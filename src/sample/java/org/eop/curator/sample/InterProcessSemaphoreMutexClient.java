package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class InterProcessSemaphoreMutexClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		//不可重入的锁
		InterProcessSemaphoreMutex mutex = new InterProcessSemaphoreMutex(client, "/mutex");
		System.out.println("当前JVM里某个线程是否持有锁：" + mutex.isAcquiredInThisProcess());
		System.out.println("---------------------开始获取锁-------------------");
		mutex.acquire();
		System.out.println("---------------------已经获取锁-------------------");
		Thread.sleep(4000);
		System.out.println("---------------------开始再次获取锁-------------------");
		mutex.acquire();
		System.out.println("---------------------已经再次获取锁-------------------");
		Thread.sleep(4000);
		System.out.println("---------------------开始释放锁-------------------");
		mutex.release();
		System.out.println("---------------------已经释放锁-------------------");
		System.out.println("---------------------开始再次释放锁-------------------");
		mutex.release();
		System.out.println("---------------------已经再次释放锁-------------------");
	}

}
