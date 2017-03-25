package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessSemaphoreV2Client5 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		SharedCount count = new SharedCount(client, "/count", 10);
		count.start();
		count.setCount(count.getCount() + 4);
		System.out.println("5增加租赁数，现在是：" + count.getCount());
		Thread.sleep(2000);
//		count.setCount(2);
//		System.out.println("5减少租赁数，现在是：" + count.getCount());
		count.close();
		client.close();
	}
}
