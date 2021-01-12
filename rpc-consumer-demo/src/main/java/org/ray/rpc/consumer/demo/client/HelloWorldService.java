package org.ray.rpc.consumer.demo.client;

import java.util.List;

import org.ray.rpc.consumer.annotation.Consumer;
import org.ray.rpc.consumer.demo.bean.User;

/**
 * HelloWorldService.java <br>
 * <br>
 * [write note]
 * 
 * @author: ray
 * @date: 2020年12月23日
 */
@Consumer(appName = "RPC-PROVIDER")
public interface HelloWorldService {
	
	public String say();

	public List<String> list(int length);

	public User getUser(String name, Boolean sex, Integer age);
}
