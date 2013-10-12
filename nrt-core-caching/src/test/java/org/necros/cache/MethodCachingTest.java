package org.necros.cache;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.necros.cache.test.MethodCallBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
public class MethodCachingTest {
	private static final Logger logger = LogManager.getLogger(MethodCachingTest.class);
	
	@Resource(name="methodCallBean")
	private MethodCallBean cachedBean;
	private MethodCallBean uncachedBean;

	@Before
	public void setUp() throws Exception {
		cachedBean.setValue(MethodCallBean.DEFAULT_VALUE);
		uncachedBean = new MethodCallBean();
		uncachedBean.setValue(MethodCallBean.DEFAULT_VALUE);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		logger.trace("Testing method result caching with both cached and uncached.");
		String valBefore, valAfter;
		logger.debug("Uncached:");
		valBefore = uncachedBean.getValue();
		logger.debug("Before setting: " + valBefore);
		assertEquals(MethodCallBean.DEFAULT_VALUE, valBefore);
		uncachedBean.setValue(MethodCallBean.CHANGED_VALUE);
		valAfter = uncachedBean.getValue();
		logger.debug("After setting: " + valAfter);
		assertEquals(MethodCallBean.CHANGED_VALUE, valAfter);
		logger.debug("Cached:");
		valBefore = cachedBean.getValue();
		logger.debug("Before setting: " + valBefore);
		assertEquals(MethodCallBean.DEFAULT_VALUE, valBefore);
		cachedBean.setValue(MethodCallBean.CHANGED_VALUE);
		valAfter = cachedBean.getValue();
		logger.debug("After setting: " + valAfter);
		assertEquals(MethodCallBean.DEFAULT_VALUE, valAfter);
	}
}
