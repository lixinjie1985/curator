package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicValue;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class DistributedAtomicValueClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final DistributedAtomicValue count = new DistributedAtomicValue(client, "/curator", new ExponentialBackoffRetry(1000, 3));
		AtomicValue<byte[]> value;
		System.out.println(count.initialize("你好".getBytes()));
		value = count.get();
		System.out.println(value.succeeded() + "," + new String(value.preValue()) + "," + new String(value.postValue()));
		value = count.trySet("我好".getBytes());
		System.out.println(value.succeeded() + "," + new String(value.preValue()) + "," + new String(value.postValue()));
		value = count.compareAndSet("我好".getBytes(), "大家好".getBytes());
		System.out.println(value.succeeded() + "," + new String(value.preValue()) + "," + new String(value.postValue()));
		client.close();
	}

}
