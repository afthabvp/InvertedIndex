package com.invertedindex.imp.cache;


import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.invertedindex.imp.cache.bean.Config;

public class SystemPropertiesReader {


	public Config readSystemConfFile() throws Exception{
		
		  
		JAXBContext jc = JAXBContext.newInstance(Config.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("systemproperties.xml");

		return (Config) unmarshaller.unmarshal(input);
		
	}

	
	

}
