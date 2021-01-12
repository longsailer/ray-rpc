package org.ray.rpc.provider;

import org.ray.rpc.provider.server.ProviderServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * NettyServerConfig.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月9日
 */
@Configuration
@ComponentScan({"org.ray"})
public class ProviderServerAutoConfigurature implements InitializingBean{
	@Value("${server.port}")
	private int port;
	@Bean
	public ProviderServer providerServer(){
		return new ProviderServer(port);
	}
	
	public void afterPropertiesSet() throws Exception {
		providerServer().start();
	}
}

