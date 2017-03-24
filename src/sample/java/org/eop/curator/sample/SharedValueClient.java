package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedValue;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class SharedValueClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final SharedValue value = new SharedValue(client, "/curator", "你好".getBytes());
		value.start();
		System.out.println("value=" + new String(value.getValue()));
		System.out.println("version=" + value.getVersionedValue().getVersion() + ",value=" + new String(value.getVersionedValue().getValue()));
		//value.setValue("大家好".getBytes());
		value.trySetValue(value.getVersionedValue(), "中国".getBytes());
		value.close();
		client.close();
	}

}
