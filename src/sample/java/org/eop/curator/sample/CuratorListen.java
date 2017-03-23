package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class CuratorListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		client.getCuratorListenable().addListener(new CuratorListener() {
			
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("节点路径：" + event.getPath());
				System.out.println("事件类型：" + event.getType());
				System.out.println("子节点：" + event.getChildren());
				System.out.println("zk事件：" + event.getWatchedEvent());
				System.out.println("--------------------------------------------");
			}
			
		});
		
		client.checkExists().watched().inBackground().forPath("/curator");
		
		synchronized (CuratorListen.class) {
			CuratorListen.class.wait();
		}
	}

}
