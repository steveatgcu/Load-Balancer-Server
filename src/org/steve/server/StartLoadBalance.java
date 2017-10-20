package org.steve.server;

import org.steve.utils.EndpointXMLUtil;
import org.steve.utils.ReadServerProperty;

import java.util.List;

import org.steve.algo.MonitorController;
import org.steve.algo.ServerBean;
import org.steve.base.Configs;
import org.steve.network.ShutDownListener;

/**
 * StartLoadBalance 启动负载均衡服务器
 * 
 * @author Ou Sheobin - me@oushaobin.cn
 * @since 1.0.0
 * @version 1
 */
public class StartLoadBalance {

	private static Configs configs;
	private static List<ServerBean> servers;

	public static void main(String[] args) {
		/* 加载主配置文件*/
		configs = ReadServerProperty.read();
		servers = EndpointXMLUtil.read();
		/* 启动监控线程*/
		Thread serverMonitor = new Thread(new MonitorController(servers));
		serverMonitor.setDaemon(true);
		serverMonitor.start();
		/* 启动主服务线程*/
		Thread mainServer = new Thread(new MainServer(configs, servers));
		mainServer.setDaemon(true);
		mainServer.start();
		/* 启动终止指令监听线程*/
		Thread shutdownThread = new Thread(new ShutDownListener(configs));
		shutdownThread.setName("Server Shutdown Listener Thread");
		shutdownThread.start();
		/* 启动完成 输出控制台欢迎信息*/
		System.out.println("Lite TCP LoadBalance Server. 2016 (c) Ou Shaobin. ");
		System.out.println("LoadBalance Server is listening to port " + configs.getListenPort());
	}

}
