package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SharedCountListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final SharedCount count = new SharedCount(client, "/curator", 0);
		count.start();
		
		count.addListener(new SharedCountListener() {
			
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
			}
			
			@Override
			public void countHasChanged(SharedCountReader shared, int count) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("version=" + shared.getVersionedValue().getVersion() + ",value=" + shared.getVersionedValue().getValue());
				System.out.println("count=" + count);
			}
		});
			
	
		
		synchronized (SharedCountListen.class) {
			SharedCountListen.class.wait();
		}
		
		count.close();
	}

}
