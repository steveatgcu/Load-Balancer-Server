package org.steve.algo;

/**
 * The Java Bean for the server records.
 * 
 * @author Ou Sheobin - me@oushaobin.cn
 * @since 1.0
 */
public class ServerBean {

	private static final int DEFAULT_WEIGHT = 1;

	private String server; // 远程服务器
	private int port; // 远程服务器监听端口
	private int weight = DEFAULT_WEIGHT; //权重
	public boolean isDown = false; //宕机检查
	public int clientPort; // 心跳客户端监听端口

	public ServerBean(String server, int port) {
		super();
		this.server = server;
		this.port = port;
	}

	public ServerBean(String server, int port, int weight) {
		super();
		this.server = server;
		this.port = port;
		this.weight = weight;
	}

	public ServerBean(String server, int port, int weight, int clientPort) {
		super();
		this.server = server;
		this.port = port;
		this.weight = weight;
		this.clientPort = clientPort;
	}

	public synchronized String getServer() {
		return server;
	}

	public synchronized void setServer(String server) {
		this.server = server;
	}

	public synchronized int getPort() {
		return port;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}

	public synchronized int getWeight() {
		return weight;
	}

	public synchronized void setWeight(int weight) {
		this.weight = weight;
	}
	
	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	@Override
	public String toString() {
		return "Server:" + server + "(port:" + port + ", weight:" + weight+')';
	}

}
