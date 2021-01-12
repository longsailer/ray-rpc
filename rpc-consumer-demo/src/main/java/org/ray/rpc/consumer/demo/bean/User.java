package org.ray.rpc.consumer.demo.bean;

import java.io.Serializable;

/**
 * User.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月31日
 */
public class User implements Serializable {

	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = -3320741861234432021L;

	private String name;
	private Boolean sex;
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}

