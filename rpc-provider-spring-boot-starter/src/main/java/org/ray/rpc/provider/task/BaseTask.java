package org.ray.rpc.provider.task;

import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.netty.channel.ChannelHandlerContext;

/**
 * BaseTask.java
 * <br><br>
 * 基础任务类，提供常用或共用的方法
 * @author: ray
 * @date: 2020年12月23日
 */
public abstract class BaseTask implements Runnable {
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected ChannelHandlerContext ctx;
	protected RpcRequestBean request;
	
	public BaseTask(ChannelHandlerContext ctx, RpcRequestBean request) throws JsonMappingException, JsonProcessingException{
		this.ctx = ctx;
		this.request = request;
	}
	
	public abstract void run();
	
	public <T> void replyBy(RpcResponseBean<T> res) throws JsonProcessingException, InterruptedException{
		ctx.writeAndFlush(res).sync();
	}
	
	public void replyByError(RpcResponseBean<String> errorRes) throws JsonProcessingException, InterruptedException{
		this.replyBy(errorRes);
	}
	
	public RpcResponseBean<String> notFound(String errMsg){
		RpcResponseBean<String> response = RPCResponseFactory.notFound(errMsg);
		return RPCResponseFactory.copyFromRequest(request, response);
	}
	
	public RpcResponseBean<String> error(String errMsg){
		RpcResponseBean<String> response = RPCResponseFactory.error(errMsg);
		return RPCResponseFactory.copyFromRequest(request, response);
	}
	
	public <T> RpcResponseBean<T> success(T result, String msg){
		RpcResponseBean<T> response = RPCResponseFactory.success(result, msg);
		return RPCResponseFactory.copyFromRequest(request, response);
	}
	
	public <T> RpcResponseBean<T> success(T d){
		return success(d, "success");
	}
	
	public void close(){
		if(ctx != null)
			ctx.close();
	}
}

