package org.ray.rpc.core;

import java.lang.reflect.Type;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcTypeReference.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月31日
 */
public class RpcTypeReference<T> extends TypeReference<T>{
	private Type type;
	public RpcTypeReference(Type type){
		this.type = type;
	}
	
	@Override
	public Type getType() {
		return type;
	}
}
