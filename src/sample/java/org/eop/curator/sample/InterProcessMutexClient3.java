package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.RevocationListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessMutexClient3 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		InterProcessMutex mutex = new InterProcessMutex(client, "/mutex");
		mutex.makeRevocable(new RevocationListener<InterProcessMutex>() {
			
			@Override
			public void revocationRequested(InterProcessMutex forLock) {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				try {
					forLock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("-------------------3开始获取一个锁------------------");
		mutex.acquire();
		System.out.println("-------------------3已经获取一个锁------------------");
		System.out.println("participant=" + mutex.getParticipantNodes());
		Thread.sleep(20000);
		System.out.println("-------------------3开始释放一个锁------------------");
		mutex.release();
		System.out.println("-------------------3已经释放一个锁------------------");
	}
}
