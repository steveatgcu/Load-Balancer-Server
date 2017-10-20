package org.steve.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.steve.base.Configs;

public class ShutDownListener implements Runnable{

	private int listenPort = 9997;
	private Configs configs;
	public static final String SHUTDOWN_COMMAND = "Server ShutDown! sdfgde23hjkdiojwi23h2rhwhd9";

	public ShutDownListener(Configs configs) {
		super();
		this.configs = configs;
		this.listenPort = configs.getStopPort();
	}

	@Override
	public void run() {
		try {
			ServerSocketChannel sc = ServerSocketChannel.open();
			sc.socket().bind(new InetSocketAddress(listenPort));
			sc.configureBlocking(false);
			while (configs.isRunning()) {
				SocketChannel scn = sc.accept();
				if(scn!=null){
					Thread pro = new Thread(new ShutdownHandler(scn.socket()));
					pro.start();
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ShutdownHandler implements Runnable{
		private Socket socket;

		public ShutdownHandler(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				InputStream ipt = socket.getInputStream();
				OutputStream opt = socket.getOutputStream();
				StringBuffer str = new StringBuffer();
				byte[] buf = new byte[1024*8];
				int len = 0;
				while((len =ipt.read(buf))!=-1){
					String temp = new String(buf,0,len);
					str.append(temp);
					System.out.println(temp);
				}
				if(str.toString().equals(SHUTDOWN_COMMAND)){
					opt.write(("Bye").getBytes());
					opt.flush();
					ipt.close();
					opt.close();
					socket.close();
					configs.setRunning(false);
				}else{
					opt.close();
					ipt.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
