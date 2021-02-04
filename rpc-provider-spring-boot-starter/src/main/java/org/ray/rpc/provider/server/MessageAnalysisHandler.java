package org.ray.rpc.provider.server;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.thread.ThreadPoolTaskExecutorBuilder;
import org.ray.rpc.provider.task.InvokeHandlerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoServerHandler.java
 * <br><br>
 * 1.@Sharable 标识这类的实例之间可以在 channel 里面共享
 * 2.日志消息输出到控制台
 * 6.关闭通道
 * @author: ray
 * @date: 2020年12月9日
 */
@Sharable
public class MessageAnalysisHandler extends ChannelInboundHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(MessageAnalysisHandler.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
        	RpcRequestBean request = null;
        	if(msg instanceof RpcRequestBean){
        		request = (RpcRequestBean) msg;
        	}else{
        		throw new IllegalArgumentException("消息类型不匹配应该是 RpcRequestBean");
        	}
			ThreadPoolTaskExecutorBuilder.build().defaultPool().execute(new InvokeHandlerTask(ctx, request));
		} catch (JsonMappingException e) {
			log.error("调用协议数据格式不正确", e);
			replyByError(ctx);
		} catch (JsonProcessingException e) {
			log.error("调用协议数据格式不正确", e);
			replyByError(ctx);
		} catch (IllegalArgumentException e) {
			log.error("调用协议数据格式不正确", e);
			replyByError(ctx);
		} finally {
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("连接["+ctx.channel().id().asShortText()+"]发生错误,原因请参考：", cause);
        ctx.close();
    }
    
    private void replyByError(ChannelHandlerContext ctx){
    	String resJson;
		try {
			RpcResponseBean<String> response = RPCResponseFactory.error("调用协议数据格式不正确");
			resJson = JsonUtils.clazzToJson(response);
			ByteBuf result = Unpooled.copiedBuffer(resJson, CharsetUtil.UTF_8);
			ctx.writeAndFlush(result);
			result.release();
		} catch (JsonProcessingException e) {
			log.error("响应调用方时失败", e);
		} finally {
			ctx.fireExceptionCaught(new JsonParseException());
		}
    }
}

