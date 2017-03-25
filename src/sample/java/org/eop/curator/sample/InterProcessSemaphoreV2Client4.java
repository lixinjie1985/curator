package org.eop.curator.sample;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class InterProcessSemaphoreV2Client4 {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		SharedCount count = new SharedCount(client, "/count", 10);
		count.start();
		//设置非固定的租赁数，当租赁用完或不够时，获取租赁将阻塞，直到有租赁被别人释放或增加租赁数
		InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, "/curator", count);
		Lease lease;
		Collection<Lease> leases;
		System.out.println("-------------------4开始获取一个租赁------------------");
		lease = semaphore.acquire();
		System.out.println("-------------------4已经获取一个租赁------------------");
		System.out.println("nodename=" + lease.getNodeName() + ",data=" + new String(lease.getData()));
		System.out.println("participant=" + semaphore.getParticipantNodes());
		Thread.sleep(4000);
		
		System.out.println("-------------------4开始获取多个租赁------------------");
		leases = semaphore.acquire(4);
		System.out.println("-------------------4已经获取多个租赁------------------");
		for (Lease lea : leases) {
			System.out.println("nodename=" + lea.getNodeName() + ",data=" + new String(lea.getData()));
		}
		System.out.println("participant=" + semaphore.getParticipantNodes());
		Thread.sleep(15000);
		
		System.out.println("-------------------4开始释放一个租赁------------------");
		semaphore.returnLease(lease);
		System.out.println("-------------------4已经释放一个租赁------------------");
		System.out.println("participant=" + semaphore.getParticipantNodes());
		
		System.out.println("-------------------4开始释放多个租赁------------------");
		semaphore.returnAll(leases);
		System.out.println("-------------------4已经释放多个租赁------------------");
		System.out.println("participant=" + semaphore.getParticipantNodes());
		System.out.println("4租赁数，现在是：" + count.getCount());
		count.close();
		client.close();
	}
}
