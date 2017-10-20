package org.steve.base;

public class Configs {

	private String listenPort;
	private String stopPort;
	private boolean isRunning = true;

	public Configs(String listenPort, String stopPort) {
		super();
		this.listenPort = listenPort;
		this.stopPort = stopPort;
	}

	public synchronized int getListenPort() {
		return Integer.valueOf(listenPort);
	}

	public synchronized int getStopPort() {
		return Integer.valueOf(stopPort);
	}

	public synchronized boolean isRunning() {
		return isRunning;
	}

	public synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public String toString() {
		return "Configs [listenPort=" + listenPort + ", stopPort=" + stopPort + "]";
	}

}
