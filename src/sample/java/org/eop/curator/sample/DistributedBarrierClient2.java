package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedBarrierClient2 {
	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		DistributedBarrier barrier = new DistributedBarrier(client, "/curator");
		barrier.setBarrier();
		System.out.println("2开始进入-------------");
		barrier.waitOnBarrier();
		System.out.println("2已经离开-------------");
	}
}

