package org.ray.rpc.server.demo;

import org.ray.rpc.server.annotation.EnableClusterServer;
import org.springframework.boot.SpringApplication;

/**
 * 系统管理中心的注册中心
 * 
 * @see EurekaServiceApplication
 * @author ray
 */
@EnableClusterServer
public class ClusterServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClusterServiceApplication.class, args);
	}
}
