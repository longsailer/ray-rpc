package org.ray.rpc.provider.demo.services;

import java.util.List;

import org.ray.rpc.provider.demo.bean.User;

/**
 * HelloWorldService.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月23日
 */
public interface HelloWorldService {
	
	public String say();
	
	public List<String> list(int length);
	
	public User getUser(String name, Boolean sex, Integer age);
}

