package com.soak.sshframe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于支持TreeNode的自动检索
 * */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface TreeNodeSupport {

	/**得到ID字段*/
	String idColumn ();
	/**得到父结点关联字段*/
	String parentIdColumn ();
}
