package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class PathChildrenCacheListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final PathChildrenCache cache = new PathChildrenCache(client, "/curator", true);
		cache.start();
		
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			//用于对一个节点的子节点进行监控，在子节点的创建、更新和删除时会收到通知
			//事件对象event参数总是有数据，所有子节点cache.getCurrentData()会动态变化
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("事件对象：" + event);
				System.out.println("所有子节点：" + cache.getCurrentData());
				System.out.println("---------------------------------");
			}
		});
		
		synchronized (PathChildrenCacheListen.class) {
			PathChildrenCacheListen.class.wait();
		}
		
		cache.close();
	}

}
