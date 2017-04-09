package com.soak.sshframe.orm;

/**
 * 类的映射
 * */
public interface BeanMapper<T> {

	/**
	 * 得到类的域映射集
	 * */
	FieldMapper<T>[] getFieldMapper();
	/**
	 * 得到表名
	 * */
	String getTable ();
	/**
	 * 得到映射的类
	 * */
	Class<T> getMapperBean();
	/**
	 * 得到主键名
	 * */
	String getPrimaryKey();
}
