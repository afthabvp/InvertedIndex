package com.invertedindex.imp.adapter.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.invertedindex.imp.common.Controller;
import com.invertedindex.imp.core.BuildIndex;


public class ApplicationLoaderAdapter extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 780262253892188721L;

	private static final Logger LOG = Logger.getLogger(ApplicationLoaderAdapter.class.getName());

	

	public void init()  throws ServletException{

		LOG.debug("**************** Loading the Application ****************");
		try {

			Controller.INSTANCE.load();
			long startTime = System.currentTimeMillis();
			BuildIndex.getInstance().startIndex();
			long endtime = System.currentTimeMillis();
			LOG.info("indexing time ="+(endtime-startTime));
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
;

	}


	public void destroy() {
		try {
			LOG.info(".......SYSTEM SHUTTING DOWN ................");

		}catch(Exception e){}
	}





}
