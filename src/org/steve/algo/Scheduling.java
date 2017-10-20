package org.steve.algo;

import java.util.List;

/**
 * The Interface of the server scheduling.
 * @author Ou Sheobin - me@oushaobin.cn
 * @since 1.0
 * @version 1.0
 */
public interface Scheduling {

	/**
	 * The method to initialization the scheduling instance.
	 * @throws Exception
	 */
	void initialization() throws Exception;

	/**
	 * Get the ServerBean from scheduling system.
	 * @return ServerBean The server info which scheduling system rutrun.
	 * @throws Exception
	 */
	ServerBean getServerBean() throws Exception;
	/**
	 * 重新计算gcdWeight和maxWeight
	 */
	public void recalParams();
	
	public List<ServerBean> getServers();
}
