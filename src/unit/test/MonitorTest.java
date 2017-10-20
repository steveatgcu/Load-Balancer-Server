package unit.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.steve.algo.MonitorController;
import org.steve.algo.ServerBean;
import org.steve.utils.EndpointXMLUtil;

public class MonitorTest {
	
	private List<ServerBean> servers;
	
	@Before
	public void init(){
		servers = EndpointXMLUtil.read();
	}
	
	@Test
	public void mainTest(){
		Thread thread = new Thread(new MonitorController(servers));
		thread.setDaemon(true);
		thread.start();
		int times = 0;
		while(times<30){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("---------Detected "+(times+1)+"---------");
			for(int i =0;i<servers.size();i++){
				System.out.println(servers.get(i));
			}
			times ++;
		}
	}

}
