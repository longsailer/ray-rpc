package org.ray.rpc.provider.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * ProviderServer.java <br>
 * <br>
 * 1.设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确） 2.呼叫服务器的 start() 方法 3.创建
 * EventLoopGroup 4.创建 ServerBootstrap 5.指定使用 NIO 的传输 Channel 6.设置 socket
 * 地址使用所选的端口 7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline 8.绑定的服务器;sync
 * 等待服务器关闭 9.关闭 channel 和 块，直到它被关闭 10.关机的 EventLoopGroup，释放所有资源。
 * 
 * @author: ray
 * @date: 2020年12月8日
 */
public class ProviderServer extends Thread {
	private Logger log = LoggerFactory.getLogger(ProviderServer.class);
	private int port;

	public ProviderServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		NioEventLoopGroup work = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, work) // 4
					.channel(NioServerSocketChannel.class) // 5
					.option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_REUSEADDR, true)

					.childOption(NioChannelOption.SO_REUSEADDR, true).childOption(NioChannelOption.TCP_NODELAY, true)
					// .childOption(ChannelOption.ALLOCATOR,
					// PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					// .childOption(ChannelOption.SO_TIMEOUT, 30)

					.localAddress(new InetSocketAddress(port)) // 6
					.childHandler(new ChannelInitializer<SocketChannel>() { // 7
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new MessageAnalysisHandler());
						}
					});
			ChannelFuture f = b.bind().sync(); // 8
			log.info("started and listen on {}", f.channel().localAddress());
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("Provider服务意外发生中断", e);
		} finally {
			try {
				group.shutdownGracefully().sync();
				work.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				log.error("Provider服务停止时发生错误", e);
			}
		}
	}
}
