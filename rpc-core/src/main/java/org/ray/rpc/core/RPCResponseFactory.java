package org.ray.rpc.core;

import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RPCResponseFactory.java
 * <br><br>
 * 构建封装返回调用结果，可以和类{@link org.ray.rpc.core.bean.RpcResponseBean }一起理解
 * @author: ray
 * @date: 2020年12月23日
 */
public class RPCResponseFactory {
	private final static int SUCCESS = 200;
	private final static int ERROR = 500;
	private final static int NOTFOUND = 404;
	
	public static <T> RpcResponseBean<T> success(String msg){
		return success(null, msg);
	}
	
	public static <T> RpcResponseBean<T> success(T result){
		return success(result, "");
	}
	
	public static <T> RpcResponseBean<T> success(T result, String msg){
		return createResponse(result, SUCCESS, msg);
	}
	
	public static <T> RpcResponseBean<T> error(String msg){
		return error(null, msg);
	}
	
	public static <T> RpcResponseBean<T> error(T result, String msg){
		return createResponse(result, ERROR, msg);
	}
	
	public static <T> RpcResponseBean<T> notFound(String msg){
		return notFound(null, msg);
	}
	
	public static <T> RpcResponseBean<T> notFound(T result, String msg){
		return createResponse(result, NOTFOUND, msg);
	}
	
	private static <T> RpcResponseBean<T> createResponse(T result, int status, String msg){
		RpcResponseBean<T> rr = new RpcResponseBean<T>();
		rr.setStatus(status);
		rr.setMsg(msg);
		rr.setData(result);
		return rr;
	}
	
	public static <T> RpcResponseBean<T> copyFromRequest(RpcRequestBean request, RpcResponseBean<T> rr){
		rr.setRequestId(request.getRequestId());
		rr.setHost(request.getHost());
		rr.setPort(request.getPort());
		rr.setApplicationName(request.getApplicationName());
		return rr;
	}
	
	public static <T> RpcResponseBean<T> processResponse(RpcResponseBean<String> source, TypeReference<T> returnType) throws Exception{
		if(source == null){
			throw new NullPointerException();
		}
		if(source.getStatus() != 200){
			throw new Exception("服务调用失败："+source.getMsg());
		}
		
		RpcResponseBean<T> target = new RpcResponseBean<T>();
		target.setStatus(source.getStatus());
		target.setRequestId(source.getRequestId());
		target.setApplicationName(source.getApplicationName());
		target.setHost(source.getHost());
		target.setPort(source.getPort());
		String object = source.getData();
		T result = JsonUtils.jsonToClazz(object, returnType);
		target.setData(result);
		return target;
	}
}

