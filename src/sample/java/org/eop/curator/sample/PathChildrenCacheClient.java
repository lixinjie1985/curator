package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author lixinjie
 */
public class PathChildrenCacheClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground().forPath("/curator", "你好".getBytes());
		System.out.println("--------------------------主节点创建-------------------------");
		Thread.sleep(2000);
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground().forPath("/curator/child", "你好".getBytes());
		System.out.println("--------------------------子节点创建-------------------------");
		Thread.sleep(2000);
		
		client.setData().withVersion(-1).inBackground().forPath("/curator/child", "我好".getBytes());
		System.out.println("--------------------------子节点更新-------------------------");
		Thread.sleep(2000);
		
		client.delete().deletingChildrenIfNeeded().withVersion(-1).inBackground().forPath("/curator/child");
		System.out.println("--------------------------子节点删除-------------------------");
		Thread.sleep(2000);
		
		client.setData().withVersion(-1).inBackground().forPath("/curator", "我好".getBytes());
		System.out.println("--------------------------主节点更新-------------------------");
		Thread.sleep(2000);
		
		client.delete().deletingChildrenIfNeeded().withVersion(-1).inBackground().forPath("/curator");
		System.out.println("--------------------------主节点删除-------------------------");
		Thread.sleep(2000);
	}

}
