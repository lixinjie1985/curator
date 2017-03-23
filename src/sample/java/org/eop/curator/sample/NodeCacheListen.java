package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class NodeCacheListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final NodeCache cache = new NodeCache(client, "/curator");
		cache.start();
		
		cache.getListenable().addListener(new NodeCacheListener() {
			
			//用于对一个节点的监控，在节点的创建、更新和删除时会收到通知
			//在节点删除时，数据为null
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("节点路径：" + (cache.getCurrentData() != null ? cache.getCurrentData().getPath() : null));
				System.out.println("节点数据：" + (cache.getCurrentData() != null ? new String(cache.getCurrentData().getData()) : null));
				System.out.println("---------------------------------");
			}
		});

		synchronized (NodeCacheListen.class) {
			NodeCacheListen.class.wait();
		}
		
		cache.close();
	}

}
