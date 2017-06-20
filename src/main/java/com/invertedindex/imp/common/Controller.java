package com.invertedindex.imp.common;



import static com.invertedindex.imp.util.GlobalLogger.LOGGER;

import com.invertedindex.imp.cache.SystemCache;

public enum Controller {
	
	INSTANCE;
	
private Controller(){
		
		LOGGER.info(" Loading Application Started");
		
		/** Loading System Cache**/
		SystemCache.INSTANCE.load();
		
		
		
	}
	
	public void load(){
		
	}
	
}
