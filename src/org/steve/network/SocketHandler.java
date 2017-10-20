package org.steve.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.log4j.Logger;
import org.steve.algo.Scheduling;
import org.steve.algo.ServerBean;

public class SocketHandler implements Runnable {

	private static Logger logger = Logger.getLogger(SocketHandler.class);

	private DataInputStream clientInputStream;
	private OutputStream clientOutputStream;
	private OutputStream remoteOutputStream;
	private InputStream remoteInputStream;

	private Socket clientSocket;
	private Socket remoteSocket;

	private Scheduling scheduler;
	private byte[] requestBody;
	private ServerBean serverBean;
	
	private int failTimes;

	private GenericKeyedObjectPool<ServerBean, Socket> pool;

	private static final String END_OF_LINE = "\r\n";

	public SocketHandler(Socket clientSocket, GenericKeyedObjectPool<ServerBean, Socket> pool, Scheduling scheduler) {
		this.clientSocket = clientSocket;
		this.scheduler = scheduler;
		this.pool = pool;
	}

	public void run() {

		try {

			clientInputStream = new DataInputStream(clientSocket.getInputStream());
			clientOutputStream = clientSocket.getOutputStream();

			proxyToRemote();
			remoteToClient();
			/* Socket Pool存在技术缺陷，暂时性停用Socket Pool*/
//			pool.returnObject(serverBean, remoteSocket);
			remoteSocket.close();
		} catch (IOException e) {
			logger.error("IOException:" + e);
		}
	}

	private void proxyToRemote() {

		try {
			int len = 0, available = -1; // 初始
			serverBean = scheduler.getServerBean();
//			remoteSocket = pool.borrowObject(serverBean);
			remoteSocket = new Socket(serverBean.getServer(),serverBean.getPort());
			logger.debug("ServerBean " + serverBean + " Socket" + remoteSocket);
			checkClientStreams();
			byte[] buf = new byte[1024 * 8]; // Buffer 缓冲区初始值 8kb
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (available != 0) {
				len = clientInputStream.read(buf);
				baos.write(buf, 0, len);
				available = clientInputStream.available();
			}
			requestBody = baos.toByteArray();
			
			logger.debug("request from client " + clientSocket.getInetAddress().getHostAddress());
			logger.debug("Request:" + new String(requestBody));

			
			remoteOutputStream = remoteSocket.getOutputStream();
			remoteOutputStream.write(requestBody);
			remoteOutputStream.flush();
			
			remoteSocket.shutdownOutput();
			
			logger.debug("Finish Flush...Wait for response");
		} catch (UnknownHostException e) {
			logger.error("Unknown Host Exception:" + serverBean, e);
		} catch (SocketException e) {
			logger.error("Socket Exception:" + serverBean, e);
			logger.debug("Request:" + new String(requestBody));
			this.failProcesser();
		} catch (IOException e) {
			logger.error("IOException:" + serverBean, e);
		} catch (Exception e) {
			logger.error("Exception:" + serverBean, e);
		}
	}

	@SuppressWarnings("deprecation")
	private void remoteToClient() {
		try {
			String line;
			DataInputStream remoteOutHeader = new DataInputStream(remoteSocket.getInputStream());
			while ((line = remoteOutHeader.readLine()) != null) {
				if (line.trim().length() == 0) {
					break;
				}
				if (line.toLowerCase().startsWith("proxy")) {
					continue;
				}
				if (line.contains("keep-alive")) {
					continue;
				}
				clientOutputStream.write(line.getBytes());
				clientOutputStream.write(END_OF_LINE.getBytes());
			}

			clientOutputStream.write(END_OF_LINE.getBytes());
			clientOutputStream.flush();

			remoteInputStream = remoteSocket.getInputStream();
			byte[] buffer = new byte[1024];

			for (int i; (i = remoteInputStream.read(buffer)) != -1;) {
				// 提升速度，每接受1kb输出1kb
				clientOutputStream.write(buffer, 0, i);
				clientOutputStream.flush();
			}
			clientSocket.shutdownOutput();
		} catch (UnknownHostException e) {
			logger.error("Unknown Host Exception:" + e);
		} catch (SocketException e) {
			logger.error("Socket Exception:" + e);
		} catch (IOException e) {
			logger.error("IOException:" + e);
		}
	}

	private void checkClientStreams() {
		try {
			if (clientSocket.isOutputShutdown()) {
				clientOutputStream = clientSocket.getOutputStream();
			}
			if (clientSocket.isInputShutdown()) {
				clientInputStream = new DataInputStream(clientSocket.getInputStream());
			}
		} catch (UnknownHostException e) {
			logger.error("Unknown Host Exception:" + e);
		} catch (SocketException e) {
			logger.error("Socket Exception:" + e);
		} catch (IOException e) {
			logger.error("IOException:" + e);
		}
	}
	
	private void failProcesser(){
		if(this.failTimes<5){
			try {
				serverBean = scheduler.getServerBean();
				remoteSocket = pool.borrowObject(serverBean);
				logger.debug("ServerBean " + serverBean + " Socket" + remoteSocket);
				logger.debug("Fail processer " + clientSocket.getInetAddress().getHostAddress());
				remoteOutputStream = remoteSocket.getOutputStream();
				remoteOutputStream.write(requestBody);
				remoteOutputStream.flush();
				logger.debug("Finish Flush...Wait for response");
			} catch (UnknownHostException e) {
				logger.error("Unknown Host Exception:" + serverBean, e);
			} catch (SocketException e) {
				logger.error("Socket Exception:" + serverBean, e);
				logger.debug("Request:" + new String(requestBody));
				this.failProcesser();
			} catch (IOException e) {
				logger.error("IOException:" + serverBean, e);
			} catch (Exception e) {
				logger.error("Exception:" + serverBean, e);
			}
			failTimes ++;
		}else{
			try {
				clientSocket.close();
			} catch (IOException e) {
				logger.error("IOException:" + e);
			}
		}
	}
}