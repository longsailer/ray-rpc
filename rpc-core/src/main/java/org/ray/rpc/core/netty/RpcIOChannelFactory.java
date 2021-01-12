package org.ray.rpc.core.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.client.ProviderInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;

/**
 * RpcIOChannal.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月29日
 */
public class RpcIOChannelFactory {
	private Logger log = LoggerFactory.getLogger(RpcIOChannelFactory.class);
	private static RpcIOChannelFactory factory = new RpcIOChannelFactory();
	private NettyClient nettyClient;
	
	private RpcIOChannelFactory(){}
	
	public static RpcIOChannelFactory getInstance(){
		return factory;
	}
	
	public RpcIOChannelFactory of(String clusterHost, int clusterPort, String appName, ChannelInboundHandler channelInboundHandler) throws IOException, IllegalStateException, InterruptedException{
		long begin = System.currentTimeMillis();
		ProviderInstance pi = this.findOneProviderInstance(clusterHost, clusterPort, appName);
		long end = System.currentTimeMillis();
		log.debug("Find provider instance total spend time : {} ms", end-begin);
		if (pi == null || pi.getHost() == null || "".equals(pi.getHost())) {
			throw new NoRouteToHostException("无可用的服务,请确保服务名正确");
		}
		return this.build(pi.getHost(), pi.getPort(), channelInboundHandler);
	}
	
	public RpcIOChannelFactory build(String host, int port, ChannelInboundHandler channelInboundHandler) throws IllegalStateException, InterruptedException{
		if(this.nettyClient == null){
			this.nettyClient = new NettyClient(System.currentTimeMillis(), host, port);
		}else if(!this.nettyClient.isSame(host, port)){
			this.nettyClient = new NettyClient(System.currentTimeMillis(), host, port);;
		}
		this.nettyClient.bootstrap(channelInboundHandler);
		return this;
	}
	
	public ChannelFuture connect() throws InterruptedException{
		return this.nettyClient.connect();
	}
	
	public void shutdown() throws InterruptedException{
		this.nettyClient.shutdown();
	}
	
	private ProviderInstance findOneProviderInstance(String clusterHost, int clusterPort, String appName) throws IOException {
		String result = "";
		String sendUrl = String.format("http://%s:%s/cluster?appName=%s", clusterHost, clusterPort, appName);
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL postUrl = new URL(sendUrl);
			// 打开连接
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			return JsonUtils.jsonToClazz(result, ProviderInstance.class);
		} catch (Exception e) {
			throw new IOException("请求服务时发生错误", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}

