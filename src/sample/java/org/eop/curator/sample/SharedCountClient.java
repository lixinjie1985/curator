package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class SharedCountClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final SharedCount count = new SharedCount(client, "/curator", 0);
		count.start();
		System.out.println("count=" + count.getCount());
		System.out.println("version=" + count.getVersionedValue().getVersion() + ",count=" + count.getVersionedValue().getValue());
		//count.setCount(10);
		count.trySetCount(count.getVersionedValue(), 30);
		count.close();
		client.close();
	}

}
