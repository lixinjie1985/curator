package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.CachedAtomicInteger;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class CachedAtomicIntegerClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final DistributedAtomicInteger count = new DistributedAtomicInteger(client, "/curator", new ExponentialBackoffRetry(1000, 3));
		CachedAtomicInteger cache = new CachedAtomicInteger(count, 100);
		AtomicValue<Integer> value;
		for (int i = 0; i < 10; i++) {
			value = cache.next();
			System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		}
		client.close();
	}

}
