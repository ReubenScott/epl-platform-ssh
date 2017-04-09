package com.soak.sshframe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface XMLParse {

	/**XML结点类型*/
	enum XMLType {
		Attribute,SimpleElement,ComplexElement
	}
	/**对应的XML的结点*/
	String key() default "";
	/***/
	XMLType type() default XMLType.Attribute;
	/**仅用于子结点集合类型*/
	String subType() default "";
}
