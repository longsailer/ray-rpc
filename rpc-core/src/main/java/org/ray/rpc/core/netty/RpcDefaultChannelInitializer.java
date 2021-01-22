package org.ray.rpc.core.netty;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * RpcDefaultChannelInitializer.java
 * <br><br>
 * @author: ray
 * @date: 2021年1月12日
 */
public class RpcDefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

	private SocketChannel ch;
	private Set<ChannelHandler> channelHandlers = new HashSet<ChannelHandler>();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		this.ch = ch;
		initChannelPipeline();
	}
	
	private void initChannelPipeline(){
		ch.pipeline().addLast(new RpcClientWriteHandler());
		if(!channelHandlers.isEmpty()){
			Iterator<ChannelHandler> its = channelHandlers.iterator();
			while(its.hasNext()){
				ch.pipeline().addLast(its.next());
			}
		}
	}
	
	public RpcDefaultChannelInitializer addLast(ChannelInboundHandler channelInboundHandler){
		channelHandlers.add(channelInboundHandler);
		return this;
	}
	
	public void clear(){
		this.channelHandlers.clear();
	}
	
	public <T extends ChannelHandler> ChannelPipeline remove(Class<T> handlerType){
		ch.pipeline().remove(handlerType);
		return ch.pipeline();
	}
}

