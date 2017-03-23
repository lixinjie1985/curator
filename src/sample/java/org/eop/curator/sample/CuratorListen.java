package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author lixinjie
 */
public class CuratorListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		client.getCuratorListenable().addListener(new CuratorListener() {
			
			//本地客户端后台任务执行完后会收到通知，前台任务不会收到通知
			//收到服务器端的Watch后也会通知，但只会通知一次，因为不会重新设置Watcher
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("节点路径：" + event.getPath());
				System.out.println("事件类型：" + event.getType());
				System.out.println("子节点：" + event.getChildren());
				System.out.println("zk事件：" + event.getWatchedEvent());
			}
			
		});
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground().forPath("/curator", "你好".getBytes());
		System.out.println("--------------------------节点创建-------------------------");
		Thread.sleep(2000);
		
		client.setData().withVersion(-1).inBackground().forPath("/curator", "我好".getBytes());
		System.out.println("--------------------------节点更新-------------------------");
		Thread.sleep(2000);
		
		client.delete().deletingChildrenIfNeeded().withVersion(-1).inBackground().forPath("/curator");
		System.out.println("--------------------------节点删除-------------------------");
		Thread.sleep(2000);
		
		client.checkExists().watched().inBackground().forPath("/curator");
		System.out.println("--------------------------查看节点是否存在-------------------------");
		Thread.sleep(2000);
		
		client.getData().watched().inBackground().forPath("/curator");
		System.out.println("--------------------------获取节点数据-------------------------");
		Thread.sleep(2000);
		
		client.getChildren().watched().inBackground().forPath("/curator");
		System.out.println("--------------------------获取子节点-------------------------");
		Thread.sleep(2000);
	}

}
