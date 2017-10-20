package org.steve.utils;

import java.util.List;

import org.steve.algo.ServerBean;

public class RoundRobinUtils {
	

	/**
	 * GCD方法 用于计算服务器权值的最大公约数
	 * 以作为服务器降低权值的步长
	 * @param servers
	 * @return
	 */
	public static int gcd(List<ServerBean> servers) {

		int min = servers.get(0).getWeight();

		for (int i = 0; i < servers.size(); i++) {
			if (servers.get(i).getWeight() < min && !servers.get(i).isDown) {
				min = servers.get(i).getWeight();
			}
		}
		while (min >= 1) {
			boolean isCommon = true;
			for (int i = 0; i < servers.size(); i++) {
				if (servers.get(i).getWeight() % min != 0) {
					isCommon = false;
					break;
				}
			}
			if (isCommon) {
				break;
			}
			min--;
		}
		return min;
	}

	/**
	 * max 最大权值搜索方法
	 * @param servers Server列表
	 * @return 最大的权值
	 */
	public static int max(List<ServerBean> servers) {
		int max = 0;
		for (int i = 0; i < servers.size(); i++) {
			if (max < servers.get(i).getWeight()) {
				max = servers.get(i).getWeight();
			}
		}
		return max;
	}

}
