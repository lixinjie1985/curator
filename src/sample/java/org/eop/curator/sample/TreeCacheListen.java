package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class TreeCacheListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final TreeCache cache = new TreeCache(client, "/curator");
		cache.start();
		
		cache.getListenable().addListener(new TreeCacheListener() {
			
			//用于对一个节点的所有子孙节点进行监控，在每一个节点的创建、更新和删除时会收到通知
			//节点存在时有数据，不存在时为null
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("事件对象：" + event);
				System.out.println("单个节点：" + cache.getCurrentData("/curator"));
				System.out.println("子孙节点：" + cache.getCurrentChildren("/curator"));
				System.out.println("---------------------------------");
			}
		});
		
		synchronized (TreeCacheListen.class) {
			TreeCacheListen.class.wait();
		}
		
		cache.close();
	}

}
