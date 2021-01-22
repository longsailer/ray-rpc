package org.ray.rpc.server.api;

import java.util.ArrayList;
import java.util.List;

/**
 * SwapeMapper.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2021年1月22日
 */
public abstract class SwapeMapper<S,T> {

	public List<T> swape(List<S> sl){
		List<T> tl = new ArrayList<T>();
		for(S s : sl){
			T t = this.mapper(s);
			tl.add(t);
		}
		return tl;
	}
	
	public abstract T mapper(S s);
}

