package com.soak.sshframe.orm;

/**
 * Class内的域到字段的映射接口
 * */
public interface FieldMapper<T> {
	
	/**
	 * 向指定对象的该域写入值
	 * */
	boolean write (T t,Object value);
	/**
	 * 得到域的值类型<br>
	 * 如果是泛型化，则返回具体类型
	 * */
	Class getFieldType ();
	/**
	 * 得到域的值
	 * */
	Object read(T t);
	/**
	 * 得到映射的字段名称
	 * */
	String getColumnName();
	/**
	 * 是否直接数据类型
	 * */
	boolean isDirectValue();
	/**仅在{@link FieldMapper#isDirectValue()}为真时使用，返回关联字段*/
	String getRelatedColumn();
	
	/**是否集合形式*/
	boolean isSet();
}
