package com.soak.sshframe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DaoSQL属性文件配置
 * */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface DaoPropConfig {

	/**是否启用Dao执行SQL配置文件*/
	boolean enable () default true;
	
	/**配置文件名<br>默认为Dao类名*/
	String propName () default "";
	/**配置文件路径<br>默认为Dao包名*/
	String propPath () default "";
}
