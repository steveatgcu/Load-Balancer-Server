package org.steve.network;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;
import org.steve.algo.ServerBean;

public class ConnectionFactory implements KeyedPooledObjectFactory<ServerBean,Socket> {
	
	private static Logger logger = Logger.getLogger(ConnectionFactory.class);

	@Override
	public PooledObject<Socket> makeObject(ServerBean server) throws Exception {
		String host = server.getServer();
		int port  =server.getPort();
		InetSocketAddress isa = new InetSocketAddress(host,port);
		Socket socket = new Socket();
		socket.connect(isa);
		logger.debug("Create Socket:"+socket);
		return new DefaultPooledObject<Socket>(socket);
	}

	@Override
	public void destroyObject(ServerBean server, PooledObject<Socket> socketPool) throws Exception {
		Socket socket = socketPool.getObject();
		if(socket!=null && !socket.isClosed()){
			logger.debug("Destory Socket:"+socket);
			socket.shutdownOutput();
			socket.shutdownInput();
			socket.close();
			socket = null;
		}
	}

	@Override
	public boolean validateObject(ServerBean server, PooledObject<Socket> socketPool) {
		Socket socket = socketPool.getObject();
		if(socket!=null && !socket.isClosed() && !socket.isConnected()){
			logger.debug("Validate:"+socket);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void activateObject(ServerBean server, PooledObject<Socket> socketPool) throws Exception {
		Socket socket = socketPool.getObject();
		logger.debug("Activate Socket:"+socket.isClosed()+"  Connect:"+socket.isConnected());
	}

	@Override
	public void passivateObject(ServerBean server, PooledObject<Socket> socketPool) throws Exception {
		
	}


}
