package org.eop.curator.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lixinjie
 */
public class DistributedAtomicIntegerClient {

	public static void main(String[] args) throws Exception {
		System.out.println("主线程：" + Thread.currentThread().getId());
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		final DistributedAtomicInteger count = new DistributedAtomicInteger(client, "/curator", new ExponentialBackoffRetry(1000, 3));
		AtomicValue<Integer> value;
		System.out.println(count.initialize(100));
		value = count.get();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.add(20);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.subtract(10);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.increment();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.decrement();
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.trySet(150);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		value = count.compareAndSet(150, 160);
		System.out.println(value.succeeded() + "," + value.preValue() + "," + value.postValue());
		client.close();
	}

}
