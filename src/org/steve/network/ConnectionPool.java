package org.steve.network;

import java.net.Socket;
import java.util.Properties;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.steve.algo.ServerBean;
import org.steve.utils.ReadServerProperty;

public class ConnectionPool  {
	
	private GenericKeyedObjectPool<ServerBean,Socket> pool;
	private ConnectionFactory connectionFactory;
	private GenericKeyedObjectPoolConfig poolConfig;
	private Properties serverCfg;
	
	public ConnectionPool(){
		init();
	}
	
	private void init(){
		serverCfg = ReadServerProperty.getProperties();
		poolConfig = new GenericKeyedObjectPoolConfig();
		poolConfig.setMaxIdlePerKey(Integer.valueOf(serverCfg.getProperty("endpoint.maxIdle")));
		poolConfig.setMaxWaitMillis(Long.valueOf(serverCfg.getProperty("endpoint.maxWait")));
		connectionFactory = new ConnectionFactory();
		pool = new GenericKeyedObjectPool<ServerBean,Socket>(connectionFactory,poolConfig);
	}
	
	private void destory(){
		pool.clear();
		if(!pool.isClosed()){
			pool.close();
		}
	}

	public GenericKeyedObjectPool<ServerBean, Socket> getPool() {
		return pool;
	}
}
