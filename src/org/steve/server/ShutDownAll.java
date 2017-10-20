package org.steve.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

import org.steve.network.ShutDownListener;

public class ShutDownAll {

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("conf/server.properties"));
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = properties.getProperty("client.ip");
		int port = Integer.valueOf(properties.getProperty("server.stopport", "9996"));
		try {
			Socket socket = new Socket(host, port);
			OutputStream opt = socket.getOutputStream();
			InputStream ipt = socket.getInputStream();			
			StringBuffer str = new StringBuffer();
			opt.write(ShutDownListener.SHUTDOWN_COMMAND.getBytes());
			opt.flush();
			socket.shutdownOutput();
			byte[] buf = new byte[1024*8];
			int len = 0;
			while((len =ipt.read(buf))!=-1){
				String temp = new String(buf,0,len);
				str.append(temp);
			}
			socket.shutdownInput();
			System.out.println(str.toString());
			opt.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
