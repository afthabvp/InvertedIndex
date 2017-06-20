package com.invertedindex.imp.adapter.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.invertedindex.imp.core.Query;



@Controller
@RequestMapping("/search/")
public class SearchController{
	
	private static final Logger LOG = Logger.getLogger(SearchController.class.getName());

	@RequestMapping(value = { "/word/q={query}"  }, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getword(@PathVariable String query) {
		long startTime = System.currentTimeMillis();

		Query q = new Query();
		
		List<String> results =  q.one_word_query(query);
		long endtime = System.currentTimeMillis();
		LOG.info("query time = "+(endtime-startTime));

		return new ResponseEntity<List<String>>(results, HttpStatus.OK);


	}
	
	@RequestMapping(value = { "/text/q={query}"  }, method = RequestMethod.GET)
	public ResponseEntity<List<String>> gettext(@PathVariable String query) {
		long startTime = System.currentTimeMillis();

		Query q = new Query();
		
		List<String> k =  q.free_text_query(query);
		long endtime = System.currentTimeMillis();
		LOG.info("query time = "+(endtime-startTime));

		return new ResponseEntity<List<String>>(k, HttpStatus.OK);


	}
	@RequestMapping(value = { "/phrase/q={query}"  }, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getphrase(@PathVariable String query) {
		long startTime = System.currentTimeMillis();

		Query q = new Query();
		
		List<String> k =  q.free_text_query(query);
		long endtime = System.currentTimeMillis();
		LOG.info("query time = "+(endtime-startTime));

		return new ResponseEntity<List<String>>(k, HttpStatus.OK);


	}


}		
