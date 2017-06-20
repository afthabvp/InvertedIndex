package com.invertedindex.imp.cache;



import static com.invertedindex.imp.util.GlobalLogger.LOGGER;

import com.invertedindex.imp.cache.bean.Config;
import com.invertedindex.imp.util.GlobalLogger;




public enum SystemCache {

	INSTANCE;
	
	private Config config; 


	private SystemCache(){
		LOGGER.info(" Loading System Cache");
		initialize();
	}
	
	public void load(){
		initialize();
	}
	
	private void initialize(){
		loadSystemPropertiesFile();
	}

	public void loadSystemPropertiesFile() {
		try {
			
			GlobalLogger.LOGGER.info(" **** Loading System Property Files ***");
			SystemPropertiesReader systemPropertiesReader = new SystemPropertiesReader();
			config = systemPropertiesReader.readSystemConfFile();
			GlobalLogger.LOGGER.info(" Loaded System Property Files : " +config.toString());
			

			
		}catch(Exception e){
			GlobalLogger.LOGGER.error(" Error While loading SystemPropertiesFile {}",e);
			e.printStackTrace();
		}
	}

	public Config getConfig() {
		return config;
	}




	




}
