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
 * 模型数据中的属性适用，设置此注解的域将作为查询条件以及映射使用<br>
 * 不设置type类型的话，默认为EQUALS
 * @author renle
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface MappedColumn {
	
	/**查询条件类型*/
	enum ConditionType {
		/**相等*/
		EQUALS{
			public String toString() {
				return "=";
			}
		},
		/**大于*/
		MORETHAN{
			@Override
			public String toString() {
				return ">";
			}
		},
		/**小于*/
		LESSTHAN{
			public String toString() {
				return "<";
			}
		},
		/**前匹配*/
		FRONTLIKE{
			public String toString() {
				return "%";
			}
		},
		/**后匹配*/
		BEHINDLIKE{
			@Override
			public String toString() {
				return "%";
			}
		};
		
	}
	
	/**
	 * 返回该域用作检索的判断条件
	 * 支持组合例如EQUALS+MORETHAN<br>
	 * 默认为相等
	 * */
	ConditionType[] searchType() default ConditionType.EQUALS;
	/**
	 * 数据库映射的字段<br>
	 * 如果为空的话，则默认等于定义的属性名
	 * */
	String mappedColumn() default "";
	/**
	 * 是否排除在查询条件之外<br>
	 * 默认不排除
	 * */
	boolean exclude() default false;
	/**
	 * 仅外键用
	 * 引用定义在{@link MappedTable}上的fk数组索引
	 * */
	//int refer() default -1;
	/**
	 * 是否在查询时使用表名修饰
	 * */
	boolean useTable() default false;
	
	/**映射的字段是否对象*/
	boolean isMappedBean() default false;
	/**
	 * 关联的属性，仅在isMappedBean=true下生效
	 * */
	String relatedColumn() default "";
}
