package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class DistributedAtomicLongClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final DistributedAtomicLong count = new DistributedAtomicLong(client, "/curator", new ExponentialBackoffRetry(1000, 3));
		AtomicValue<Long> value;
		System.out.println(count.initialize(100L));
		value = count.get();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.add(20L);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.subtract(10L);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.increment();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.decrement();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.trySet(150L);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.compareAndSet(150L, 160L);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		client.close();
	}

}
