package org.ray.rpc.consumer.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.ray.rpc.consumer.demo.bean.User;
import org.ray.rpc.consumer.demo.client.HelloWorldService;
import org.ray.rpc.core.thread.ThreadPoolTaskExecutorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TestService.java <br>
 * <br>
 * [write note]
 * 
 * @author: ray
 * @date: 2020年12月25日
 */
@Component
public class TestService {
	@Autowired
	private HelloWorldService helloWorldService;
	/*@PostConstruct
	public void test() {
		for (int i = 0; i < 100; i++) {
			final int index = i;
			executorService.execute(() -> {
				long begin = System.currentTimeMillis();
				String result = helloWorldService.say();
				System.out.println("1.[" + index + "]" + result);
				long end = System.currentTimeMillis();
				System.out.println("say Total:"+(end-begin));
			});
		}
	}

	@PostConstruct
	public void test1() {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			List<String> result = helloWorldService.list(10);
			System.out.println("2.[" + i + "]" + String.join(",", result.toArray(new String[result.size()])));
		}
		long end = System.currentTimeMillis();
		System.out.println("list Total:"+(end-begin));
	}*/
	@PostConstruct
	public void test2() {
		List<Future<User>> fList = new ArrayList<Future<User>>();
		for (int i = 0; i < 1000; i++) {
			final int index = i;
			Future<User> future = ThreadPoolTaskExecutorBuilder.build().definedPool(5, 5, 5, 60, "testthread_").submit(() -> {
				try{
					User user = helloWorldService.getUser("张三", true, index);
					return user;
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			});
			fList.add(future);
		}
		
		for(Future<User> future : fList){
			try {
				User user  = future.get();
				System.out.println("User: " + user.getName() + "," + user.getAge());
				this.count();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int count=0;
	public synchronized void count(){
		count++;
		System.out.println(count);
	}
}
