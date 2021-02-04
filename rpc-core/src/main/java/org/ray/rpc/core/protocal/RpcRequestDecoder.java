package org.ray.rpc.core.protocal;

import java.util.List;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
 * RpcDecoder.java
 * <br><br>
 * 请求实体解码器
 * @author: ray
 * @date: 2021年2月1日
 */
public class RpcRequestDecoder extends ByteToMessageDecoder {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in == null || !in.isReadable() || in.readableBytes() <= 0){
			log.warn("接收到的信息为空.", new Throwable("The bytebuf is empty in RpcRequestDecoder.decode."));
			return;
		}
		String message = in.toString(CharsetUtil.UTF_8);
		if(message.indexOf("}{") != -1){
			message = message.replaceAll("\\}\\{", "}##{");
		}
		String[] requestBeanAry = message.split("##");
		for(String requestStr : requestBeanAry){
			RpcRequestBean request = JsonUtils.jsonToClazz(requestStr, new TypeReference<RpcRequestBean>() {});
			out.add(request);
		}
		in.clear();
	}
}

