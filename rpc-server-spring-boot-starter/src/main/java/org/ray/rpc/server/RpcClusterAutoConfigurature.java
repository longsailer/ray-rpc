package org.ray.rpc.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebSecurityConfig.java <br>
 * <br>
 * @author: ray
 * @date: 2020年1月19日
 */
@Configuration
@EnableWebSecurity
@ComponentScan("org.ray.rpc")
public class RpcClusterAutoConfigurature extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/cluster").permitAll()
		.and().csrf().disable();
		super.configure(http);
	}
}
