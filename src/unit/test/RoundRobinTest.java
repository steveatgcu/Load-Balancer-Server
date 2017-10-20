package unit.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.steve.algo.Scheduling;
import org.steve.algo.ServerBean;
import org.steve.algo.WeightRoundRobin;
import org.steve.utils.EndpointXMLUtil;

public class RoundRobinTest {
	
	private static Scheduling wrb;

	static{
		wrb = new WeightRoundRobin(EndpointXMLUtil.read());
	}
	
	Map<ServerBean,Integer> count = new HashMap<ServerBean,Integer>();

	@Test
	public void test() {
		try {
			ExecutorService pool = Executors.newFixedThreadPool(1);
			for (int i = 0; i < 3500; i++) {
				Runnable run = new Runnable() {
					public void run() {
						try {
							ServerBean s = wrb.getServerBean();
							if(count.containsKey(s)){
								count.put(s, count.get(s)+1);
							}else{
								count.put(s, 1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				pool.execute(run);
			}
			pool.shutdown();
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			System.out.println("完成模拟访问3500次，算法调度统计如下:");
			for(ServerBean s:count.keySet()){
				System.out.println(s+" 访问次数："+count.get(s));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}