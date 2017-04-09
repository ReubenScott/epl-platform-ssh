package com.soak.sshframe.map;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.soak.sshframe.annotation.MappedColumn;
import com.soak.sshframe.annotation.MappedTable;
import com.soak.sshframe.orm.BeanMapper;
import com.soak.sshframe.orm.FieldMapper;
import com.soak.sshframe.orm.StrategySelector;

/**
 * 映射对象类
 * 用于处理实现了{@link MappedTable}以及{@link MappedColumn}<br>
 * 注解的对象到数据库操作的转化
 * */
public class MappedObject<T> implements BeanMapper<T>{

	
	private static Map<Class,MappedObject> store=new Hashtable<Class, MappedObject>();
	private static String template = "select COLUMN from TABLE where CONDITION";
	private static String insertTemplate = "insert into TABLE (COLUMN) values (PARAMS)";
	private static String updateTemplate = "update TABLE set COLUMN where CONDITION";
	
	private String selectColumns;
	private String table;
	private String pk;
	
	/**
	 * 根据注解MappedTable和MappedColumn实现接口
	 * */
	private MappedObject(Class<T> cls){
		
		this.cls = cls;
		MappedTable mt = (MappedTable)cls.getAnnotation(MappedTable.class);
		if (mt == null) {
			// 映射表不存在，非支持的JavaBean模型
			throw new RuntimeException("Unsupported model by type <"+cls.getName()+"> .Please implement annotation MappedTable.");
		}
		
		table = mt.table();
		pk = mt.pk();
		
		fieldList = new ArrayList<MappedField<T>>();
		
		boolean flag;
		for (Field f: cls.getDeclaredFields()){
			//添加情况一
			flag = false;
			if (mt.defaultMapped()){
				MappedColumn mc = f.getAnnotation(MappedColumn.class);
				if (mc==null || !mc.exclude()){
					flag = true;
				}
			}else{
				if (f.getAnnotation(MappedColumn.class)!=null ){
					flag = true;
				}
			}
			if (flag){
				fieldList.add(StrategySelector.getInstance(MappedField.class, f));
			}
		}
		
	}
	
	private Class<T> cls;
	private List<MappedField<T>> fieldList;
	
	/**
	 * 得到一个指定类的映射实例
	 * */
	synchronized public static <T> MappedObject<T> getInstance(Class<T> cls){
		if (!store.containsKey(cls)){
			MappedObject<T> mo= new MappedObject<T>(cls);
			store.put(cls, mo);
		}
		return store.get(cls);
	}
	
	/**
	 * 得到一个指定类的映射实例
	 * */
	synchronized public static <T> MappedObject<T> getInstance(String clsName){
		try {
			return (MappedObject<T>) getInstance(Class.forName(clsName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FieldMapper[] getFieldMapper() {
		return fieldList.toArray(new FieldMapper[0]);
	}

	public Class getMapperBean() {
		return cls;
	}

	public String getPrimaryKey() {
		return pk;
	}

	public String getTable() {
		return table;
	}
	
	
	
}
