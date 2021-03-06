package org.ray.rpc.core.protocal;

import org.ray.rpc.core.JsonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * RpcEncoder.java
 * <br><br>
 * 写入对象进行编译
 * @author: ray
 * @date: 2021年2月1日
 */
public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse<String>> {
	@Override
	protected void encode(ChannelHandlerContext ctx, RpcResponse<String> msg, ByteBuf out) throws Exception {
		String response = JsonUtils.clazzToJson(msg);
		out.writeCharSequence(response, CharsetUtil.UTF_8);
	}
}

