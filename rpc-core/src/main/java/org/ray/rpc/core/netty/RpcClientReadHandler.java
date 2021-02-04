package org.ray.rpc.core.netty;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.selector.RpcResponseBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoClientHandler.java <br>
 * <br>
 * 共享客户端结果接收处理器
 * @author: ray
 * @date: 2020年12月11日
 */
@Sharable
public class RpcClientReadHandler extends ChannelInboundHandlerAdapter {

	private Logger log = LoggerFactory.getLogger(RpcClientReadHandler.class);
	private RpcResponseBlockingQueue<String> queue = RpcResponseBlockingQueue.getInstance();

	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			RpcResponseBean<String> response = null;
			if(msg instanceof RpcResponseBean){
				response = (RpcResponseBean<String>) msg;
			}else{
				String responseStr = ((ByteBuf)msg).toString(CharsetUtil.UTF_8);
				response = JsonUtils.jsonToClazz(responseStr, new TypeReference<RpcResponseBean<String>>() {});
			}
			if(response != null){
				queue.add(response);
			}
		} catch(Exception e){
			log.error("消息接收时发生异常.", e);
		} finally {
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("连接ID为【" + ctx.channel().id().asShortText() + "】发生错误，详情：", cause); // 5
		ctx.close();
	}
}
