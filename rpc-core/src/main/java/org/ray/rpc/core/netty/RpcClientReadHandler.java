package org.ray.rpc.core.netty;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.RpcResponseBlockingQueue;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.client.RpcResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoClientHandler.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月11日
 */
@Sharable
public class RpcClientReadHandler<T> extends ChannelInboundHandlerAdapter{
	
	private Logger log = LoggerFactory.getLogger(RpcClientReadHandler.class);
	
	private ChannelHandlerContext ctx;
	private RpcResponseBean<String> response;
	private RpcResponseListener listener;
	private volatile boolean loading = true;
	private RpcResponseBlockingQueue<String> queue = RpcResponseBlockingQueue.getInstance();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		super.channelActive(ctx);
		log.debug("I'm active.");
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String result = in.toString(CharsetUtil.UTF_8);
        log.debug("Client received: {}, Data: {}", ctx.channel().id().asShortText(), result);        //2
        try {
			this.response = JsonUtils.jsonToClazz(result, new TypeReference<RpcResponseBean<String>>(){});
			queue.add(this.response);
			ctx.close();
		} catch (JsonMappingException e) {
			log.error("结果格式解析错误", e);
		} catch (JsonProcessingException e) {
			log.error("结果格式解析错误", e);
		} finally {
			in.release();
			this.end();
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
    	log.error("连接【"+ctx.channel().id().asShortText()+"】发生错误，详情：", cause);                //5
        ctx.close();                            //6
        this.end();
    }
    
    private void end(){
		this.loading = false;
		if(listener != null) listener.onGetResponse();
    }
    
    public void close(){
    	if(this.ctx != null)
    		this.ctx.close();
    }

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public RpcResponseBean<String> getResponse() {
		return response;
	}

	public boolean isLoading() {
		return loading;
	}

	public RpcResponseListener getListener() {
		return listener;
	}

	public RpcClientReadHandler<T> setListener(RpcResponseListener listener) {
		this.listener = listener;
		return this;
	}
}

