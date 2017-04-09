package com.soak.sshframe.map.substrategy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.soak.framework.reflect.ReflectTool;
import com.soak.sshframe.map.MappedField;

/**
 * Setter方式实现<br>
 * Setter方式用于有时Bean有一些自己的逻辑处理
 * */
public class SetterMappedField<T> extends MappedField<T> {

	public SetterMappedField(Field f) {
		super(f);
	}

	/**
	 * 写入方式
	 * */
	public boolean write(T t, Object value) {
		Method m =ReflectTool.getSetterMethod(t.getClass(), field.getName(), field.getType());
		if (m==null)return false;
		try {
			m.invoke(t, value);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Object read(T t) {
		
		Object value = null;
		Method m =ReflectTool.getGetterMethod(t.getClass(), field.getName());
		if (m==null)return value;
		try {
			value = m.invoke(t);
			return value;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}
}
