package org.ray.rpc.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EnableClusterServer.java
 * <br><br>
 * 开启RPC服务的功能，该服务基于Eureka的功能而实现，借助于其注册中心的功能和服务发现功能，实现RPC服务的注册、发现和负载的功能
 * @author: ray
 * @date: 2020年12月31日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableEurekaServer
@SpringBootApplication
public @interface EnableClusterServer {
}