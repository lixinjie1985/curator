package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class InterProcessReadWriteLockClient3 {
	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		InterProcessReadWriteLock readwrite = new InterProcessReadWriteLock(client, "/readwrite");
		InterProcessMutex write = readwrite.writeLock();
		InterProcessMutex read = readwrite.readLock();
		System.out.println("--------------------------开始获取写锁-------------------------");
		//先获取写锁
		write.acquire();
		System.out.println("--------------------------已经获取写锁-------------------------");
		System.out.println("--------------------------开始获取读锁-------------------------");
		//在获取写锁的情况下可以再获取读锁，反之不行
		read.acquire();
		System.out.println("--------------------------已经获取读锁-------------------------");
		Thread.sleep(15000);
		System.out.println("--------------------------开始释放写锁-------------------------");
		//写锁被释放后，锁被降级为读锁，其它等待获取读锁的JVM现在可以同时获取
		write.release();
		System.out.println("--------------------------已经释放写锁-------------------------");
		Thread.sleep(15000);
		System.out.println("--------------------------开始释放读锁-------------------------");
		read.release();
		System.out.println("--------------------------已经释放读锁-------------------------");
	}
}
