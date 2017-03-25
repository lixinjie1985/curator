package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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
		InterProcessMutex mutex = new InterProcessMutex(client, "/mutex");
		System.out.println("participant=" + mutex.getParticipantNodes());
		System.out.println("-------------------4开始撤销一个锁------------------");
		Revoker.attemptRevoke(client, "/mutex/_c_08c6029c-979a-4554-a659-26d534dc480b-lock-0000000002");
		System.out.println("-------------------4已经撤销一个锁------------------");
		Thread.sleep(1000);
	}
}
