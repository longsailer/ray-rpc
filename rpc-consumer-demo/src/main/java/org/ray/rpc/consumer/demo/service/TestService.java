package org.ray.rpc.consumer.demo.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.ray.rpc.consumer.demo.bean.User;
import org.ray.rpc.consumer.demo.client.HelloWorldService;
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
	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
		for (int i = 0; i < 1000; i++) {
			final int index = i;
			executorService.execute(() -> {
				try{
					long begin = System.currentTimeMillis();
					User user = helloWorldService.getUser("张三", true, index);
					long end = System.currentTimeMillis();
					System.out.println("3.[" + index + "]: " + user.getName() + "," + user.getAge() + " getUser Total:"+(end-begin));
				}catch(Exception e){
					e.printStackTrace();
				}
			});
		}
	}
}
