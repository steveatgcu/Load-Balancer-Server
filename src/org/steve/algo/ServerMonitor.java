package org.steve.algo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ServerMonitor implements Runnable {
	
	private static Logger logger = Logger.getLogger(ServerMonitor.class);
	
	private ServerBean server;

	public ServerMonitor(ServerBean server) {
		super();
		this.server = server;
	}
	

	@Override
	public void run() {
		boolean isOpen = checkPort();
		if(isOpen){
			int rank = refreshRank();
			if(rank!=0){
				server.setDown(false);
				server.setWeight(rank);
			}else{
				server.setDown(true);
				server.setWeight(1);
			}
		}else{
			server.setDown(true);
			server.setWeight(0);
		}
	}

	private int refreshRank() {
		int port = server.getClientPort();
		String host = server.getServer();
		try {
			Socket socket = new Socket(host, port);
			OutputStream opt = socket.getOutputStream();
			String command = "ReAd SyStEm StatuS...";
			InputStream ipt = socket.getInputStream();			
			StringBuffer str = new StringBuffer();
			opt.write(command.getBytes());
			opt.flush();
			socket.shutdownOutput();
			byte[] buf = new byte[1024*8];
			int len = 0;
			while((len =ipt.read(buf))!=-1){
				String temp = new String(buf,0,len);
				str.append(temp);
			}
			socket.shutdownInput();
			opt.close();
			socket.close();
			JSONObject jsonData = JSON.parseObject(str.toString());
			return jsonData.getIntValue("currentRank");
		} catch (IOException e) {
			
		}
		return 0;
	}

	private boolean checkPort() {
		Socket checkSocket = new Socket();
		boolean isOpen = true;
		try {
			checkSocket.connect(new InetSocketAddress(server.getServer(), server.getPort()));
			checkSocket.close();
		} catch (IOException e) {
			isOpen = false;
			logger.info("Server:"+server.getServer()+"(Port:"+server.getPort()+") is down.");
		}
		return isOpen;
	}

}
