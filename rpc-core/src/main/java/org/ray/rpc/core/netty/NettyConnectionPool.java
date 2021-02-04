package org.ray.rpc.core.netty;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.util.concurrent.ConcurrentHashMap;

import org.ray.rpc.core.bean.ProviderInstance;
import org.ray.rpc.core.ins.ServiceInstanceContext;

import io.netty.channel.ChannelInboundHandler;

/**
 * RpcIOChannal.java <br>
 * <br>
 * 连接池
 * @author: ray
 * @date: 2020年12月29日
 */
public class NettyConnectionPool {
	private static NettyConnectionPool factory = new NettyConnectionPool();
	private ConcurrentHashMap<String, NettyClient> connectPool = new ConcurrentHashMap<String, NettyClient>();

	private NettyConnectionPool() {
	}

	public static NettyConnectionPool getInstance() {
		return factory;
	}

	public NettyClient create(String appName) throws IOException, IllegalStateException, InterruptedException {
		return this.create(appName, null);
	}

	public NettyClient create(String appName, ChannelInboundHandler channelInboundHandler)
			throws IOException, IllegalStateException, InterruptedException {
		ServiceInstanceContext context = ServiceInstanceContext.getInstance();
		ProviderInstance pi = context.balance(appName);
		if (pi == null || pi.getHost() == null || "".equals(pi.getHost())) {
			throw new NoRouteToHostException("无可用的服务,请确保服务名正确");
		}
		if (channelInboundHandler == null) {
			return this.build(appName, pi.getHost(), pi.getPort());
		}
		return this.build(appName, pi.getHost(), pi.getPort(), channelInboundHandler);
	}

	public NettyClient build(String appName, String host, int port) throws IllegalStateException, InterruptedException {
		return this.build(appName, host, port, null);
	}

	public NettyClient build(String appName, String host, int port, ChannelInboundHandler channelInboundHandler)
			throws IllegalStateException, InterruptedException {
		String connectKey = appName + host + port;
		NettyClient nettyClient = null;
		if (connectPool.containsKey(connectKey)) {
			nettyClient = connectPool.get(connectKey);
		} else {
			nettyClient = new NettyClient(System.currentTimeMillis(), host, port);
			nettyClient.bootstrap();
			connectPool.put(connectKey, nettyClient);
		}
		if (channelInboundHandler != null) {
			nettyClient.bootstrap(channelInboundHandler);
		}
		nettyClient.connect();
		return nettyClient;
	}
}
