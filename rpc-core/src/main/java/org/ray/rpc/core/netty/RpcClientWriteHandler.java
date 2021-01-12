package org.ray.rpc.core.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * EchoClientWriteHandler.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月11日
 */
@Sharable
public class RpcClientWriteHandler extends ChannelOutboundHandlerAdapter{
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		ctx.writeAndFlush(msg, promise);
	}
}

