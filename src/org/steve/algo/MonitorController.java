package org.steve.algo;

import java.util.List;

public class MonitorController implements Runnable{
	
	public List<ServerBean> servers; //Endpoint Servers


	public MonitorController(List<ServerBean> servers) {
		super();
		this.servers = servers;
	}


	@Override
	public void run() {
		while(true){
			for(int i=0;i<servers.size();i++){
				Thread thread = new Thread(new ServerMonitor(servers.get(i)));
				thread.setName("Server Monitor - "+i);
				thread.setDaemon(true);
				thread.start();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
