package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedValue;
import org.apache.curator.framework.recipes.shared.SharedValueListener;
import org.apache.curator.framework.recipes.shared.SharedValueReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SharedValueListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final SharedValue value = new SharedValue(client, "/curator", "你好".getBytes());
		value.start();
		
		value.getListenable().addListener(new SharedValueListener() {
			
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
			}
			
			@Override
			public void valueHasChanged(SharedValueReader shared, byte[] value) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("version=" + shared.getVersionedValue().getVersion() + ",value=" + new String(shared.getVersionedValue().getValue()));
				System.out.println("value=" + new String(value));
			}
		});
			
	
		
		synchronized (SharedValueListen.class) {
			SharedValueListen.class.wait();
		}
		
		value.close();
		client.close();
	}

}
