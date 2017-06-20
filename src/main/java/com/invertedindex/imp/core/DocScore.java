package com.invertedindex.imp.core;

public class DocScore implements Comparable<DocScore> {
	double score;
	String docId;

	public DocScore(double score, String docId) {
		this.score = score;
		this.docId = docId;
	}

	public int compareTo(DocScore docScore) {
		if (score > docScore.score) return -1;
		if (score < docScore.score) return 1;
		return 0;
	}
}