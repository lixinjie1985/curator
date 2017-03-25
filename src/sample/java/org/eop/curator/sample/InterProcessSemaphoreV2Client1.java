package org.eop.curator.sample;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessSemaphoreV2Client1 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		//设置固定的租赁数，当租赁用完或不够时，获取租赁将阻塞，直到有租赁被别人释放
		InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, "/curator", 10);
		Lease lease;
		Collection<Lease> leases;
		System.out.println("-------------------1开始获取一个租赁------------------");
		lease = semaphore.acquire();
		System.out.println("-------------------1已经获取一个租赁------------------");
		System.out.println("nodename=" + lease.getNodeName() + ",data=" + new String(lease.getData()));
		System.out.println("participant=" + semaphore.getParticipantNodes());
		Thread.sleep(4000);
		
		System.out.println("-------------------1开始获取多个租赁------------------");
		leases = semaphore.acquire(6);
		System.out.println("-------------------1已经获取多个租赁------------------");
		for (Lease lea : leases) {
			System.out.println("nodename=" + lea.getNodeName() + ",data=" + new String(lea.getData()));
		}
		System.out.println("participant=" + semaphore.getParticipantNodes());
		Thread.sleep(15000);
		
		System.out.println("-------------------1开始释放一个租赁------------------");
		semaphore.returnLease(lease);
		System.out.println("-------------------1已经释放一个租赁------------------");
		System.out.println("participant=" + semaphore.getParticipantNodes());
		
		System.out.println("-------------------1开始释放多个租赁------------------");
		semaphore.returnAll(leases);
		System.out.println("-------------------1已经释放多个租赁------------------");
		System.out.println("participant=" + semaphore.getParticipantNodes());
	}
}
