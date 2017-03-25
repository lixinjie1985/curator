package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessMutexClient2 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		InterProcessMutex mutex = new InterProcessMutex(client, "/mutex");
		System.out.println("-------------------2开始获取一个锁------------------");
		mutex.acquire();
		System.out.println("-------------------2已经获取一个锁------------------");
		Thread.sleep(15000);
		System.out.println("-------------------2开始释放一个锁------------------");
		mutex.release();
		System.out.println("-------------------2已经释放一个锁------------------");
	}
}
