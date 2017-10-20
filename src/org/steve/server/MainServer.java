package org.steve.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.log4j.Logger;
import org.steve.algo.Scheduling;
import org.steve.algo.ServerBean;
import org.steve.algo.WeightRoundRobin;
import org.steve.base.Configs;
import org.steve.network.ConnectionPool;
import org.steve.network.SocketHandler;

public class MainServer implements Runnable {

	private Configs configs;
	private ServerSocket server;
	private Scheduling scheduler;
	private ExecutorService executor;
	private List<ServerBean> servers;
	private ConnectionPool connectionPool;
	private GenericKeyedObjectPool<ServerBean, Socket> pool;

	private static Logger logger = Logger.getLogger(MainServer.class);

	public MainServer(Configs configs,List<ServerBean> servers) {
		this.configs = configs;
		this.servers = servers;
		init();
	}
	
	private void init(){
		scheduler = new WeightRoundRobin(servers);
		executor = Executors.newCachedThreadPool();
		connectionPool = new ConnectionPool();
		pool = connectionPool.getPool();
		try {
			server = new ServerSocket(configs.getListenPort());
		} catch (IOException e) {
			logger.error("Catch exception while trying to start Load Balancer server:", e);
		}
	}
	

	@Override
	public void run() {
		while (configs.isRunning()) {
			try {
				executor.execute(new SocketHandler(server.accept(), pool, scheduler));
			} catch (IOException e) {
				logger.error("Catch exception while trying to process sockets:", e);
			}
		}
	}

}
