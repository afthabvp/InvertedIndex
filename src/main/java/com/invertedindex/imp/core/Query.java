package com.invertedindex.imp.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class Query {


	private Map<String, Map<String, LinkedList<Integer>>> invertedIndex;
	private Map<String, Map<String, LinkedList<Integer>>> regindex;
	Map<String,LinkedList<Double>> docVectors = null;
	private BuildIndex index ;

	public Query(){
		this.index = BuildIndex.getInstance();
		this.invertedIndex = index.getTotalIndex();
		this.docVectors = index.getDocVectors();
		this.regindex = index.getRegdex();
		
	}



	public int queryFreq(String term,String query){
		int count = 0;
		String[] arr = query.split("\\W+");
		for (String word:arr){
			if(word.equals(term))
				count += 1;
		}
		return count;
	}
	private List<Double> termfreq(Set<String> terms, String query) {
		List<Double>temp = new ArrayList<Double>();
		for (String term : terms){
			temp.add((double) queryFreq(term, query));
		}
		return temp;
	}

	public List<Double> queryVec(String query){
		String[] arr = query.split("\\W+");
		List<Double> queryvec = new ArrayList<Double>();
		for(String word : arr){
			queryvec.add((double) queryFreq(word,query));
		}
		Set<String> unique_keys = invertedIndex.keySet();
		Map<String,Double> query_idf = new HashMap<String,Double>();
		for(String term : unique_keys){
			query_idf.put(term, this.index.getIdf().get(term));
		}
		double mag = Math.sqrt(queryvec.stream().map((x) -> x*x).reduce((sum, x) -> sum + x).get()); 
		List<Double> freq = termfreq(invertedIndex.keySet(),query);
		List<Double> tf = new ArrayList<Double>();
		List<Double> out = new ArrayList<Double>();
		for(Double x : freq ){
			tf.add(x/mag);
		}
		for(int i= 0;i< invertedIndex.size();i++){
			out.add(tf.get(i)*freq.get(i));
		}
		return out;

	}

	private double dotProduct(LinkedList<Double> vec, List<Double> queryVec) {
		double sum = 0;
		if(vec.size() != queryVec.size()){
			return sum;
		}
		for( int i = 0;i<vec.size();i++){
			sum +=  vec.get(i)*queryVec.get(i);
		}
		return sum;
	}
	public List<String> rankResults(List<String> resultDocs,String query){
		List<DocScore> results = new ArrayList<DocScore>();
		List<Double> queryVec = queryVec(query);
		for(String file : resultDocs){
			
			DocScore temp = new DocScore(dotProduct(this.docVectors.get(file),queryVec),file);
			results.add(temp);
		}
		Collections.sort(results);
		List<String> out = new ArrayList<String>();
		for( DocScore x : results){
			out.add(x.docId);
		}
		return out;
	}

	public List<String> one_word_query(String word){
		StringUtils.strip(word);
		String[] arr = word.split("\\W+");
		StringBuilder builder = new StringBuilder();
		for(String s : arr) {
			builder.append(s);
		}
		String str = builder.toString().toLowerCase();
		if(invertedIndex.get(str) != null){
			List<String> files = new ArrayList<String>();
			for(String filename : invertedIndex.get(str).keySet())
				files.add(filename);
			return rankResults(files,str);
		}

		return null;
	}


	public List<String> free_text_query(String string){
		List<String> results = new ArrayList<String>();
		String[] arr = string.split("\\W+");
		for(String word : arr){
			List<String> list= one_word_query(word);
			if (list != null)
				results.addAll(list);
		}
		return rankResults(results, string);
	}


	public static void main(String[] args) throws IOException{
		List<File> file = new ArrayList<File>();
		BuildIndex.getInstance().startIndex();
		Query q = new Query();

		List<String> k =  q.one_word_query("sample");
		for(String s: k){
			System.out.println(s);
		}

	}

}