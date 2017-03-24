package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedDoubleBarrierClient1 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, "/curator", 2);
		System.out.println("1开始进入-------------");
		barrier.enter();
		System.out.println("1已经进入-------------");
		Thread.sleep(2000);
		System.out.println("1开始离开-------------");
		barrier.leave();
		System.out.println("1已经离开-------------");
	}

}
