package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author lixinjie
 */
public class CuratorListenClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/curator", "你好".getBytes());
		System.out.println("--------------------------节点创建-------------------------");
		Thread.sleep(15000);
		
		client.setData().withVersion(-1).forPath("/curator", "我好".getBytes());
		System.out.println("--------------------------节点更新-------------------------");
		Thread.sleep(15000);
		
		client.delete().deletingChildrenIfNeeded().withVersion(-1).forPath("/curator");
		System.out.println("--------------------------节点删除-------------------------");
		Thread.sleep(15000);
	}

}
