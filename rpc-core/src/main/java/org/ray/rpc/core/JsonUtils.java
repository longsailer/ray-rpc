package org.ray.rpc.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonUtils.java
 * <br><br>
 * Java类与Json字符串之间相互转化
 * @author: ray
 * @date: 2020年12月23日
 */
public class JsonUtils {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static String clazzToJson(Object t) throws JsonProcessingException{
		return mapper.writeValueAsString(t);
	}
	
	public static <T> T jsonToClazz(String str, Class<T> clazz) throws JsonMappingException, JsonProcessingException{
		return mapper.readValue(str, clazz);
	}
	
	public static <T> T jsonToClazz(String str, TypeReference<T> type) throws JsonMappingException, JsonProcessingException{
		return mapper.readValue(str, type);
	}
}
