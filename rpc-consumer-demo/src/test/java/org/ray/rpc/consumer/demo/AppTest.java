package org.ray.rpc.consumer.demo;

import java.lang.reflect.Method;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.RpcTypeReference;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.tcp.RpcResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public void testApp() throws NoSuchMethodException, SecurityException {
	}
}
