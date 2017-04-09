/**
 * 
 */
package com.soak.sshframe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JavaBean映射表
 * @author renle
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface MappedTable {

	/**
	 * 对应的数据库的表名
	 * */
	String table() /*default ""*/;
	
	/**
	 * PK
	 * */
	String pk() default "id";
	/**
	 * adapter
	 * 是否默认匹配全属性
	 * */
	boolean defaultMapped() default false;
	/**
	 * 外键
	 * 格式为table.pk
	 * */
	//String[] fk() default "";
}
