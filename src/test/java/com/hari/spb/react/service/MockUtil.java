package com.hari.spb.react.service;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class MockUtil {

	public static final void setPrivateField(Object target, String fieldName, Object newValue) {
		 if (AopUtils.isAopProxy(target) && target instanceof Advised) {
			 try {
				 target = ((Advised) target).getTargetSource().getTarget();
			 } catch (Exception e) {
				 throw new RuntimeException(e);
			 }
		  }
		 ReflectionTestUtils.setField(target, fieldName, newValue);
	}

}