package org.steve.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.steve.algo.ServerBean;

public class EndpointXMLUtil {
	
	public static List<ServerBean> read(){
		List<ServerBean> serverList = new ArrayList<ServerBean>();
		        
	    try {
	    	SAXReader reader = new SAXReader();  
			Document   document = reader.read(new File("conf/serverlist.xml"));
			Element root = document.getRootElement(); 
			List<Element> nodes = root.elements("server");
			Iterator<Element> iter = nodes.iterator();
			while(iter.hasNext()){
				Element serverNode = iter.next();
				int port =Integer.valueOf(serverNode.elementText("port"));
				int weight = Integer.valueOf(serverNode.elementText("weight"));
				int clientPort = Integer.valueOf(serverNode.elementText("clientPort"));
				String host = serverNode.elementText("host");
				serverList.add(new ServerBean(host,port,weight,clientPort));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		return serverList;
	}


}
