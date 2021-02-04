package org.ray.rpc.core.protocal;

import java.util.List;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.RpcResponseBean;
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
 * [write note]
 * @author: ray
 * @date: 2021年2月1日
 */
public class RpcResponseDecoder extends ByteToMessageDecoder {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in == null || !in.isReadable() || in.readableBytes() <= 0){
			log.warn("接收到的信息为空.", new Throwable("The bytebuf is empty in RpcResponseDecoder.decode."));
			return;
		}
		String message = in.toString(CharsetUtil.UTF_8);
		RpcResponseBean<String> response = JsonUtils.jsonToClazz(message, new TypeReference<RpcResponseBean<String>>() {});
		out.add(response);
		in.clear();
	}
}

