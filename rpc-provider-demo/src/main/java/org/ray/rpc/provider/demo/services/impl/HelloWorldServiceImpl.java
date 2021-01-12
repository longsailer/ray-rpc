package org.ray.rpc.provider.demo.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ray.rpc.provider.annotation.Provider;
import org.ray.rpc.provider.demo.bean.User;
import org.ray.rpc.provider.demo.services.HelloWorldService;

/**
 * HelloWorldService.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月23日
 */
@Provider("HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService{

	public String say() {
		return "Hello World ! I get it, yeah !!!";
	}

	public List<String> list(int length) {
		if(length < 0){
			return Collections.emptyList();
		}
		String[] s = new String[length];
		for(int i=0; i<length; i++){
			s[i] = String.valueOf(i);
		}
		return Arrays.asList(s);
	}

	public User getUser(String name, Boolean sex, Integer age) {
		User user = new User();
		user.setName(name);
		user.setSex(sex);
		user.setAge(age);
		return user;
	}
}

