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
		final Thread mainThread = Thread.currentThread();
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		InterProcessMutex mutex = new InterProcessMutex(client, "/mutex");
		mutex.makeRevocable(new RevocationListener<InterProcessMutex>() {
			
			@Override
			public void revocationRequested(InterProcessMutex forLock) {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				try {
					//这里直接调用报错，需要持有锁的线程调用才行
					//forLock.release();
					//只要线程调用了mutex.acquire()方法，那么就参与到获取锁的队伍当中，就会有一个节点与之对应
					//无论是获得了锁还是被阻塞等待获得锁，都可以被撤销，需要指定与之对应的节点来告知撤销哪个JVM里的锁
					System.out.println("锁是否被当前JVM里某个线程持有：" + forLock.isAcquiredInThisProcess());
					//检测锁是否被当前JVM里的线程持有，然后对应不同的撤销逻辑
					if (forLock.isAcquiredInThisProcess()) {
						mainThread.interrupt();//持有锁，主线程响应中断，释放锁
					} else {
						mainThread.interrupt();//不持有锁，主线程响应中断，放弃获得锁
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("-------------------3开始获取一个锁------------------");
		mutex.acquire();
		System.out.println("-------------------3已经获取一个锁------------------");
		System.out.println("participant=" + mutex.getParticipantNodes());
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			System.out.println("提前释放锁线程：" + Thread.currentThread().getId());
			System.out.println("-------------------3开始提前释放一个锁------------------");
			mutex.release();
			System.out.println("-------------------3已经提前释放一个锁------------------");
			return;
		}
		System.out.println("-------------------3开始释放一个锁------------------");
		mutex.release();
		System.out.println("-------------------3已经释放一个锁------------------");
		System.out.println("participant=" + mutex.getParticipantNodes());
		Thread.sleep(20000);
	}
}
