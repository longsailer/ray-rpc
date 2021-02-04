package org.ray.rpc.provider.task;

import java.lang.reflect.Method;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.provider.context.SpringApplicationContext;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.netty.channel.ChannelHandlerContext;

/**
 * InvokeHandler.java
 * <br><br>
 * 通过类名与方法名，调用本地方法，把结果封装后返回
 * @author: ray
 * @date: 2020年12月23日
 */
public class InvokeHandlerTask extends BaseTask{
	private SpringApplicationContext context = SpringApplicationContext.getInstance();
	
	public InvokeHandlerTask(ChannelHandlerContext ctx, RpcRequestBean request)
			throws JsonMappingException, JsonProcessingException {
		super(ctx, request);
	}

	public void run() {
		try{
			if(StringUtils.isEmpty(request.getServiceName()) || StringUtils.isEmpty(request.getMethodName()) || StringUtils.isEmpty(request.getApplicationName())){
				throw new IllegalArgumentException("参数传递不完整");
			}
			
			Object providerService = context.getBean(request.getServiceName());
			if(providerService == null){
				providerService = context.getBean(StringUtils.uncapitalize(request.getServiceName()));
			}
			if(providerService == null){
				replyByError(notFound("未找到指定的服务"));
				return;
			}
			Method providerMethod = null;
			Method[] methods = providerService.getClass().getMethods();
			for(Method method : methods){
				if(request.getMethodName().equals(method.getName())){
					providerMethod = method;
					break;
				}
			}
			Object result = null;
			if(providerMethod != null){
				result = providerMethod.invoke(providerService, request.getParamValues());
				if(result != null && !String.class.equals(result.getClass())){
					result = JsonUtils.clazzToJson(result);
				}
				replyBy(success(result));
			}else{
				throw new NoSuchMethodException("未找方法:"+request.getMethodName());
			}
		}catch(Exception e){
			log.error("服务调用失败:", e);
			try {
				replyByError(error("服务调用失败"));
			} catch (Exception pe) {
				log.error("返回结果失败:", pe);
			}
		}finally{
		}
	}
}

