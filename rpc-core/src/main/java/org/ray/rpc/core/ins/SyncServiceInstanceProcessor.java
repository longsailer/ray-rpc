package org.ray.rpc.core.ins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.ProviderInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * SyncServiceInstanceProcessor.java
 * <br><br>
 * 同步注册服务上所有服务实例清单
 * @author: ray
 * @date: 2021年1月22日
 */
public class SyncServiceInstanceProcessor implements InitializingBean{
	private static Logger log = LoggerFactory.getLogger(SyncServiceInstanceProcessor.class);
	private String clusterHost;
	private int clusterPort;

	private ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(2);
	
	public SyncServiceInstanceProcessor(String clusterHost, int clusterPort){
		this.clusterHost = clusterHost;
		this.clusterPort = clusterPort;
	}
	
	public void afterPropertiesSet() throws Exception {
		log.debug("即将开始同步注册服务清单...");
		Runnable syncTasker = new Runnable() {
			public void run() {
				try{
					ServiceInstanceContext context = ServiceInstanceContext.getInstance();
					Map<String, List<ProviderInstance>> piMap = findAllProviderInstance();
					context.setServiceCache(piMap);
					log.debug("同步注册服务清单完成");
				}catch(Exception e){
					log.error("同步注册服务实例时发生错误", e);
				}
			}
		};
		syncTasker.run();
		threadPool.scheduleWithFixedDelay(syncTasker, 3, 5, TimeUnit.SECONDS);
	}
	
	private Map<String, List<ProviderInstance>> findAllProviderInstance() throws IOException {
		String result = "";
		String sendUrl = String.format("http://%s:%s/cluster/all", clusterHost, clusterPort);
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
			return JsonUtils.jsonToClazz(result, new TypeReference<Map<String, List<ProviderInstance>>>() {});
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

