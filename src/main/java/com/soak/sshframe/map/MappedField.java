package com.soak.sshframe.map;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import com.soak.framework.reflect.ReflectTool;
import com.soak.sshframe.annotation.MappedColumn;
import com.soak.sshframe.annotation.StrategySupport;
import com.soak.sshframe.orm.FieldMapper;

/**
 * Java对象域的映射
 * */
@StrategySupport(configPath="com\\temobi\\base\\core\\myconfig.xml", id="setterType")
abstract public class MappedField<T> implements FieldMapper<T> {

	/**
	 * 域
	 * */
	protected Field field;
	/**
	 * 该域到数据库字段的映射值
	 * */
	private String column;
	/**
	 * 支持域到字段转化的桥梁
	 * */
	private MappedColumn mc;
	
	/**是否直接映射到数据库的字段*/
	private boolean isDirectValue = true;
	/**对于关联的Bean的关联域*/
	private String relatedField;
	/**是否泛型化*/
	private boolean isGeneric = false;
	
	private Class real ;
	
	
	public MappedField(Field f) {
		field = f;
		mc=field.getAnnotation(MappedColumn.class);
		//this.tableName=tableName;
		if (mc!=null && mc.isMappedBean()){
			isDirectValue = false;
			relatedField = mc.relatedColumn();
		}
		
		if (mc==null || mc.mappedColumn().length()==0){
			column = field.getName();
		}else
		{
			column = mc.mappedColumn();
		}
		
		if (f.getGenericType() instanceof ParameterizedType){
			isGeneric = true;
			real = ReflectTool.getGenericType(f);
		}
		
		/*if (this.tableName!=null && this.tableName.length()>0 && mc!=null && mc.useTable()){
			column=this.tableName+"."+column;
		}*/
		
	}


	public Class getFieldType() {
		return isGeneric?real:field.getType();
	}


	public String getColumnName() {
		return column;
	}
	
	public String getFieldName() {
		return field.getName();
	}
	
	public boolean isDirectValue() {
		return isDirectValue;
	}
	
	public String getRelatedColumn() {
		return relatedField;
	}
	
	public boolean isSet() {
		return isGeneric;
	}
}
