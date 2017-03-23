package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class ConnectionStateListen {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			
			//首次启动连上服务器后是CONNECTED，把服务器关闭后连接断开是SUSPENDED
			//然后重启动服务器重新连上是RECONNECTED，且使用原来的Session ID
			//若服务器长时间不启动则丢失是LOST，此时重启动服务器重连上
			//则创建一个全新的Session，新的Session ID
			//若客户端断开后长时间不连，服务器将把Session超时
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState state) {
				System.out.println("事件线程：" + Thread.currentThread().getId());
				System.out.println("连接状态：" + state);
			}
			
		});
		
		synchronized (ConnectionStateListen.class) {
			ConnectionStateListen.class.wait();
		}
	}

}
