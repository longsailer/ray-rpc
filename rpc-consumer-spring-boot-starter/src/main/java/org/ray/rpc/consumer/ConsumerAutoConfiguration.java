package org.ray.rpc.consumer;

import org.ray.rpc.core.client.RpcClient;
import org.ray.rpc.core.client.TcpRpcClient;
import org.ray.rpc.core.ins.SyncServiceInstanceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

@Configuration
@AutoConfigureOrder
@ComponentScan("org.ray.rpc.consumer")
@ConditionalOnClass({AutoConfiguredConsumerScannerRegistrar.class})
@EnableConfigurationProperties(ConsumerProperties.class)
public class ConsumerAutoConfiguration implements EnvironmentAware{
	private static Logger log = LoggerFactory.getLogger(ConsumerAutoConfiguration.class);
	private Environment environment;

	@Bean
	public AutoConfiguredConsumerScannerRegistrar consumerScannerRegistrar(){
		log.debug("Scan consumer and registried in spring container...");
		String basePackage = environment.getProperty("rpc.consumer.consumerLocation");
		Assert.hasText(basePackage, "The value of property rpc.consumer.consumerLocation is required.");
		RpcClient client = rpcClient();
		return new AutoConfiguredConsumerScannerRegistrar(basePackage, client);
	}
	
	@Bean
	@ConditionalOnClass(SyncServiceInstanceProcessor.class)
	public SyncServiceInstanceProcessor serviceInstanceProcessor(){
		String[] clusterInfo = this.clusterInfo();
		int port = Integer.parseInt(clusterInfo[1]);
		return new SyncServiceInstanceProcessor(clusterInfo[0], port);
	}
	
	@Bean
	public RpcClient rpcClient(){
		String[] clusterInfo = this.clusterInfo();
		int port = Integer.parseInt(clusterInfo[1]);
		RpcClient client = new TcpRpcClient(clusterInfo[0], port);
		return client;
	}
	
	private String[] clusterInfo(){
		String eurekaDefaultZone = environment.getProperty("eureka.client.serviceUrl.defaultZone");
		Assert.hasText(eurekaDefaultZone, "The value of property eureka.client.serviceUrl.defaultZone is required.");
		int begin = eurekaDefaultZone.indexOf("@");
		if(begin == -1){
			begin = eurekaDefaultZone.indexOf("//");
		}
		int end = eurekaDefaultZone.indexOf("/", begin+3);
		String[] clusterInfo = eurekaDefaultZone.substring(begin+1, end).split(":");
		return clusterInfo;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}