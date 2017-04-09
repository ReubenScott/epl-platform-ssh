package com.soak.sshframe.map.substrategy;

import java.lang.reflect.Field;

import com.soak.sshframe.map.MappedField;

/**
 * 使用直接方式来实现域的赋值
 * */
public class DirectMappedField<T> extends MappedField<T> {

	public DirectMappedField(Field f) {
		super(f);
	}

	/**
	 * 写入方式
	 * */
	public boolean write(T t,Object value){
		try {
			field.setAccessible(true);
			field.set(t, value);
			field.setAccessible(false);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Object read(T t) {
		Object obj = null;
		try {
			field.setAccessible(true);
			obj = field.get(t);
			field.setAccessible(false);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
