package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.Revoker;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessMutexClient4 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		System.out.println("-------------------4开始撤销一个锁------------------");
		Revoker.attemptRevoke(client, "/mutex/_c_a72604d7-ab2d-4b81-ba48-1af5f354f580-lock-0000000000");
		System.out.println("-------------------4已经撤销一个锁------------------");
		Thread.sleep(15000);
	}
}
