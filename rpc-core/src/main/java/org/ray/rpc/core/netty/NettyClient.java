package org.ray.rpc.core.netty;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.protocal.RpcRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

/**
 * NettyClient.java <br>
 * <br>
 * 
 * @author: ray
 * @date: 2020年12月9日
 */
public class NettyClient {
	private final String host;
	private final int port;
	private long id;
	private Bootstrap b;
	private EventLoopGroup group;
	private RpcDefaultChannelInitializer channelInitializer;
	private ChannelFuture channelFuture;

	public NettyClient(long id, String host, int port) {
		this.host = host;
		this.port = port;
		this.id = id;
		this.channelInitializer = new RpcDefaultChannelInitializer();
		this.group = new NioEventLoopGroup();
		this.b = new Bootstrap();
		this.b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(channelInitializer);
	}

	public long getId() {
		return this.id;
	}
	
	public Bootstrap bootstrap() throws IllegalStateException, InterruptedException{
		return this.bootstrap(RpcClientReadHandlerFactory.createReadHandler());
	}

	public Bootstrap bootstrap(ChannelInboundHandler channelInboundHandler)
			throws IllegalStateException, InterruptedException {
		this.channelInitializer.clear();
		this.channelInitializer.addLast(channelInboundHandler);
		return this.b;
	}

	/**
	 * 创建Bootstrap,对于通道的初始化可以参考以下代码：
	 * 
	 * <PRE>
	 * new ChannelInitializer&lt;SocketChannel&gt;() {
	         <code>@Override</code>
	         public void initChannel(SocketChannel ch) 
	             throws Exception {
	             ch.pipeline().addLast(new RpcClientReadHandler());
	             ch.pipeline().addLast(new RpcClientWriteHandler());
	         }
	     }
	 * </PRE>
	 * 
	 * @author ray
	 * @param address
	 * @param channelInitializer
	 * @throws Exception
	 * @return Bootstrap
	 * @throws InterruptedException
	 */
	public Bootstrap bootstrap(ChannelInitializer<SocketChannel> channelInitializer)
			throws IllegalStateException, InterruptedException {
		try {
			b.handler(channelInitializer);
			return b;
		} catch (Exception e) {
			throw new IllegalStateException("创建netty客户端失败.");
		}
	}

	public ChannelFuture connect() throws InterruptedException {
		this.channelFuture = b.connect(host, port).sync();
		return this.channelFuture;
	}

	public ChannelFuture connect(String host, int port) throws InterruptedException {
		this.channelFuture = b.connect(host, port).sync();
		return this.channelFuture;
	}
	
	public void writeAndFlush(RpcRequest request) throws JsonProcessingException, InterruptedException {
		this.writeAndFlush(this.channelFuture, request);
	}

	public void writeAndFlush(ChannelFuture f, RpcRequest request) throws JsonProcessingException, InterruptedException {
		String requestJson = JsonUtils.clazzToJson(request);
		ByteBuf requestBuf = Unpooled.copiedBuffer(requestJson, CharsetUtil.UTF_8);
		f.channel().writeAndFlush(requestBuf).sync();
	}

	public void shutdown() throws InterruptedException {
		group.shutdownGracefully().sync();
	}

	public boolean isSame(String host, int port) {
		if (StringUtil.isNullOrEmpty(this.host)) {
			return false;
		}
		return this.host.equals(host) && this.port == port;
	}
}
