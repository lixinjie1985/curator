package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class InterProcessReadWriteLockClient2 {
	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		InterProcessReadWriteLock readwrite = new InterProcessReadWriteLock(client, "/readwrite");
		//写锁一次只能被一个JVM获取
		InterProcessMutex write = readwrite.writeLock();
		System.out.println("--------------------------开始获取写锁-------------------------");
		write.acquire();
		System.out.println("--------------------------已经获取写锁-------------------------");
		Thread.sleep(15000);
		System.out.println("--------------------------开始释放写锁-------------------------");
		write.release();
		System.out.println("--------------------------已经释放写锁-------------------------");
	}
}
