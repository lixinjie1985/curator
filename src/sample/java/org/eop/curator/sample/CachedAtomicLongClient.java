package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.CachedAtomicLong;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class CachedAtomicLongClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final DistributedAtomicLong count = new DistributedAtomicLong(client, "/curator", new ExponentialBackoffRetry(1000, 3));
		CachedAtomicLong cache = new CachedAtomicLong(count, 100);
		AtomicValue<Long> value;
		for (int i = 0; i < 10; i++) {
			value = cache.next();
			System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		}
		client.close();
	}

}
