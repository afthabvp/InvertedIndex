package com.invertedindex.imp.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class BuildIndex {
	
	private static BuildIndex  _instance = new BuildIndex();

	private TreeMap<String,Integer> df = new TreeMap<String,Integer>();
	private TreeMap<String,Double> idf = new TreeMap<String,Double>();
	TreeMap<String,Map<String,Double>> tf = new TreeMap<String,Map<String,Double>>();

	Map<String,String[]> file_to_terms = new HashMap<String,String[]>();

	private List<File> files;
	private List<String> filenames = new ArrayList<String>();
	private Map<String,Map<String,LinkedList<Integer>>> regdex;
	private Map<String, Map<String, LinkedList<Integer>>> totalIndex;
	private  Map<String, LinkedList<Integer>> vectors;
	Map<String,LinkedList<Double>> docVectors = null;
	private Map<String, Double> mags;
	
	public static BuildIndex getInstance() {
		return _instance;
	}
	
	public void startIndex() throws IOException{
		
		List<File> file = new ArrayList<File>();
		try (Stream<Path> filePathStream=Files.walk(Paths.get("C:\\Users\\afthab.ahammad\\Desktop\\redis-unstable"))){
				//Paths.get(SystemCache.INSTANCE.getConfig().getFilePath()))) {
			filePathStream.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					file.add(new File(filePath.toString()));
				}
			});
		}
		this.files = file;
		process_files();
		this.regdex = make_indices(this.file_to_terms);
		this.setTotalIndex(fullIndex());
		this.vectors = vectorize();
		this.mags = magnitudes();
		populateScores();
		this.docVectors = make_vectors();
	}


	private void process_files(){
		for (File file :  this.files){
			filenames.add(file.getName());
			String[] lines;
			try {
				lines = new String(Files.readAllBytes(Paths.get(file.getPath()))).split("\\W+");	
				this.file_to_terms.put(file.getName(), lines);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//input = {filename: [word1, word2, ...], ...}
	//res = {filename: {word: [pos1, pos2, ...]}, ...}
	private Map<String,Map<String,LinkedList<Integer>>> make_indices(Map<String,String[]> file_to_terms){
		Map<String,Map<String,LinkedList<Integer>>> total = new HashMap<String,Map<String,LinkedList<Integer>>>();
		for (Map.Entry<String, String[]> entry : file_to_terms.entrySet()){
			total.put(entry.getKey(),index_one_file(entry.getValue()));
		}
		return total;
	}

	//input = [word1, word2, ...]
	//output = {word1: [pos1, pos2], word2: [pos2, pos434], ...}
	Map<String,LinkedList<Integer>> index_one_file(String[] termlist){
		Map<String,LinkedList<Integer>> fileIndex = new HashMap<String,LinkedList<Integer>>();
		int i = 0;
		for(String word : termlist){
			if(fileIndex.get(word) != null && !fileIndex.get(word).equals("")){
				LinkedList<Integer> temp = fileIndex.get(word);
				temp.add(i);
				fileIndex.put(word, temp);
			}else{
				LinkedList<Integer> temp =new LinkedList<Integer>();
				temp.add(i);
				fileIndex.put(word, temp);
			}
			i++;

		}
		return fileIndex;
	}

	//input = {filename: {word: [pos1, pos2, ...], ... }}
	//res = {word: {filename: [pos1, pos2]}, ...}, ...}
	private Map<String, Map<String, LinkedList<Integer>>> fullIndex(){
		Map<String,Map<String,LinkedList<Integer>>> indie_indices = this.regdex;

		Map<String,Map<String,LinkedList<Integer>>> total_index = new HashMap<String,Map<String,LinkedList<Integer>>>() ;

		for (Entry<String, Map<String, LinkedList<Integer>>> indie_indices_entry : indie_indices.entrySet()){
			String filename = indie_indices_entry.getKey();

			Map<String,LinkedList<Integer>> value = indie_indices_entry.getValue();
			for (Entry<String, LinkedList<Integer>> entry : value.entrySet()){
				Map<String,Double> temp = new HashMap<String,Double>();
				temp.put(entry.getKey(),(double) entry.getValue().size());
				tf.put(filename, temp);
				if(df.get(entry.getKey()) != null && ! df.get(entry.getKey()).equals("") )
					df.put(entry.getKey(),df.get(entry.getKey())+1);
				else
					df.put(entry.getKey(), 1);
				Map<String,LinkedList<Integer>> index  = null;
				if(total_index.get(entry.getKey()) != null && ! total_index.get(entry.getKey()).equals("") ){
					if(total_index.get(entry.getKey()).get(filename) != null && ! total_index.get(entry.getKey()).get(filename).equals("") ){
						index = total_index.get(entry.getKey());
						LinkedList<Integer> temp1 = total_index.get(entry.getKey()).get(filename);
						temp1.addAll(entry.getValue());
						index.put(filename, temp1);
					}else{
						index  = total_index.get(entry.getKey());
						index.put(filename, entry.getValue());
					}
				}else{
					index = new HashMap<String,LinkedList<Integer>>();
					index.put(filename, entry.getValue());

				}
				total_index.put(entry.getKey(),index);
			}
		}			    
		return total_index;
	}


	private Map<String, LinkedList<Integer>> vectorize(){

		Map<String,LinkedList<Integer>> vectors = new HashMap<String,LinkedList<Integer>>();
		for(String file:filenames){
			Map<String, LinkedList<Integer>> word_c = regdex.get(file);
			LinkedList<Integer> temp = new LinkedList<Integer>();
			for (Entry<String,LinkedList<Integer>> entry : word_c.entrySet()){
				temp.add(entry.getValue().size());
			}
			vectors.put(file, temp);
		}
		return vectors;
	}
	private Map<String,Double>  magnitudes(){
		Map<String,Double> mags = new HashMap<String,Double>();
		for (File file : files){
			LinkedList<Integer> vect = this.vectors.get(file.getName());
			double mag = Math.sqrt(vect.stream().map((x) -> x*x).reduce((sum, x) -> sum + x).get()); 			
			mags.put(file.getName(),mag);

		}
		return mags;
	}


	private double term_frequency(String word, String filename){

		if(tf.get(filename).get(word) != null && !tf.get(filename).get(word).equals("")){
			return (tf.get(filename).get(word))/(mags.get(filename));
		}else{
			return 0;
		}

	}

	private int collection_size(){
		return files.size();
	}

	private double idf_func(double N,double N_t){
		if (N_t != 0)
			return Math.log(N/N_t);
		else
			return 0;
	}

	private int document_frequency(String term){
		for(String key : this.getTotalIndex().keySet()){
			if(term.equals(key))
				return this.getTotalIndex().get(key).size();
			else
				return 0;
		}
		return 0;

	}	
	private void  populateScores(){
		Map<String, LinkedList<Integer>> temp = new HashMap<String, LinkedList<Integer>>();

		for(String file:filenames){
			temp = this.regdex.get(file);
			for(String word : temp.keySet()){
				tf.get(file).put(word, term_frequency(word, file));
				if(df.get(word) != null && !df.get(word).equals(""))
					idf.put(word, idf_func(collection_size(), df.get(word)));
				else{
					idf.put(word,0.0);
				}
			}
		}
	}
	
	public Map<String, LinkedList<Double>> make_vectors(){
		Map<String,LinkedList<Double>> vectors = new HashMap<String,LinkedList<Double>>();
		for(String file:filenames){
			Set<String> unique_keys = getTotalIndex().keySet();
			LinkedList<Double> docvec = new LinkedList<Double>();
			for(String term : unique_keys){
				docvec.add(generateScores(term, file));
			}
			vectors.put(file, docvec);

		}
		return vectors;
	}

	public double  generateScores(String term,String filename){
		if(tf.get(filename) == null && tf.get(filename).equals(""))
			return 0.0;
		else if(tf.get(filename).get(term) == null)
			return 0.0;
		else return (tf.get(filename).get(term) * idf.get(term));

	}

	public Map<String, Map<String, LinkedList<Integer>>> getTotalIndex() {
		return totalIndex;
	}


	public void setTotalIndex(Map<String, Map<String, LinkedList<Integer>>> totalIndex) {
		this.totalIndex = totalIndex;
	}


	public Map<String, Map<String, LinkedList<Integer>>> getRegdex() {
		return regdex;
	}


	public void setRegdex(Map<String, Map<String, LinkedList<Integer>>> regdex) {
		this.regdex = regdex;
	}


	public Map<String, Double> getIdf() {
		return idf;
	}

	public Map<String, LinkedList<Integer>> getVectors() {
		return vectors;
	}

	public Map<String, LinkedList<Double>> getDocVectors() {
		return docVectors;
	}
	


}