package org.steve.algo;

import java.util.List;

import org.apache.log4j.Logger;
import org.steve.utils.EndpointXMLUtil;
import org.steve.utils.RoundRobinUtils;

/**
 * Scheduling 调度器的一个实现方法 Auto Weighted Round Robin
 * <p>
 * <strong>Weighted round robin (WRR)</strong> is a network scheduling
 * discipline.
 * </p>
 * 
 * @author Ou Sheobin - me@oushaobin.cn
 */
public class WeightRoundRobin implements Scheduling {

	private boolean isInit = false;
	public List<ServerBean> servers;
	public int currentWeight = 0;
	public int serverIndex = -1;
	public int maxWeight;
	public int gcdWeight;

	private static Logger logger = Logger.getLogger(WeightRoundRobin.class);
	
	public WeightRoundRobin(List<ServerBean> servers){
		try {
			this.servers = servers;
			initialization();
		} catch (Exception e) {
			logger.error("Exception:",e);
		}
	}

	@Override
	public void initialization() throws Exception {
		if (!isInit) {
			this.maxWeight = RoundRobinUtils.max(servers);
			this.gcdWeight = RoundRobinUtils.gcd(servers);
		} else {
			throw new Exception("Scheduling has already started.");
		}
	}

	@Override
	public synchronized ServerBean getServerBean() {
		return GetBestServer();
	}

	private ServerBean GetBestServer() {
		while (true) {
			serverIndex = (serverIndex + 1) % servers.size();
			if (serverIndex == 0) {
				currentWeight = currentWeight - gcdWeight;
				if (currentWeight < 0) {
					currentWeight = maxWeight;
					 if(currentWeight ==0){
							return null; 
					}
				}
			}
			if (servers.get(serverIndex).getWeight() >= currentWeight){
				return servers.get(serverIndex);
			}
		}
	}

	public synchronized List<ServerBean> getServers() {
		return servers;
	}

	public synchronized void setServers(List<ServerBean> servers) {
		this.servers = servers;
	}
	
	/**
	 * 重新计算gcdWeight和maxWeight
	 */
	public void recalParams(){
		this.maxWeight = RoundRobinUtils.max(servers);
		this.gcdWeight = RoundRobinUtils.gcd(servers);
	}

}
