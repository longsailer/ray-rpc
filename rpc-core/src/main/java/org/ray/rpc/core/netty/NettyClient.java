package org.ray.rpc.core.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;

/**
 * NettyClient.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月9日
 */
public class NettyClient {
	private final String host;
    private final int port;
    private long id;
    private Bootstrap b;
    private EventLoopGroup group;
    private Object lock = new Object();
    
    public NettyClient(long id, String host, int port) {
        this.host = host;
        this.port = port;
        this.id = id;
    }
    
    public long getId(){
    	return this.id;
    }
    
    public Bootstrap bootstrap(ChannelInboundHandler channelInboundHandler) throws IllegalStateException, InterruptedException{
    	return bootstrap(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) 
                throws Exception {
                ch.pipeline().addLast(channelInboundHandler);
                ch.pipeline().addLast(new RpcClientWriteHandler());
            }
        });
    }
    
    public Bootstrap bootstrap(ChannelInitializer<SocketChannel> channelInitializer) throws IllegalStateException, InterruptedException{
    	return bootstrap(this.host, this.port, channelInitializer);
    }
    		
    
    public Bootstrap bootstrap(String host, int port, ChannelInitializer<SocketChannel> channelInitializer) throws IllegalStateException, InterruptedException{
    	return bootstrap(new InetSocketAddress(host, port), channelInitializer);
    }
    /**
     * 创建Bootstrap,对于通道的初始化可以参考以下代码：
     * <PRE>new ChannelInitializer&lt;SocketChannel&gt;() {
             <code>@Override</code>
             public void initChannel(SocketChannel ch) 
                 throws Exception {
                 ch.pipeline().addLast(new RpcClientReadHandler());
                 ch.pipeline().addLast(new RpcClientWriteHandler());
             }
         }</PRE>
     * @author ray
     * @param address
     * @param channelInitializer
     * @throws Exception
     * @return Bootstrap
     * @throws InterruptedException 
     */
    public Bootstrap bootstrap(InetSocketAddress address, ChannelInitializer<SocketChannel> channelInitializer) throws IllegalStateException, InterruptedException {
        try {
        	synchronized (lock) {
        		if(this.b == null){
            		group = new NioEventLoopGroup();
            		b = new Bootstrap();                //1
            		b.group(group)                                //2
            		.channel(NioSocketChannel.class)            //3
            		.remoteAddress(address)    //4
            		.option(ChannelOption.TCP_NODELAY, true)
            		.handler(channelInitializer);
            	}
			}
        	if(!isSame(address.getHostString(), address.getPort())){
        		b.remoteAddress(address);
        	}
        	b.handler(channelInitializer);
            return b;
        } catch(Exception e){
        	throw new IllegalStateException("创建netty客户端失败.");
        }
    }
    
    public ChannelFuture connect() throws InterruptedException{
    	ChannelFuture f = b.connect().sync();        //6
        return f;
    }
    
    public void shutdown() throws InterruptedException{
    	group.shutdownGracefully().sync();
    }
    
    public boolean isSame(String host, int port){
    	if(StringUtil.isNullOrEmpty(this.host)){
    		return false;
    	}
    	return this.host.equals(host) && this.port == port;
    }
}

