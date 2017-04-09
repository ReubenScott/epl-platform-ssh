package com.soak.sshframe.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.soak.framework.reflect.BeanInjecter;
import com.soak.framework.reflect.ReflectTool;
import com.soak.sshframe.annotation.XMLParse;
import com.soak.sshframe.model.SelectBean;

//import com.soak.base.anno.XMLParse;
//import com.soak.base.core.DefaultRowMapper;
//import com.soak.base.core.reflect.BeanInjecter;
//import com.soak.base.core.reflect.ReflectTool;
//import com.soak.base.model.SelectBean;

public class XMLReader {

	private SAXReader reader;
	private Document doc;
	
	public XMLReader (String url){
		reader = new SAXReader();
		try {
			doc = reader.read(new File(url));
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	public XMLReader(InputStream is){
		reader = new SAXReader();
		try {
			doc = reader.read(is);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到以某个名称命名的元素列表
	 * */
	public List<Element> getElementList (String eleName){
		Element el = doc.getRootElement();
		List<Element> list = el.elements(eleName);
		return list;
	}
	
	public Map<String,String> getElemetAttribute(Element ele){
		
		Map<String,String> map = new HashMap<String, String>();
		Iterator<Attribute> it = ele.attributeIterator();
		while (it.hasNext()){
			Attribute at = it.next();
			map.put(at.getName(), at.getValue());
		}
		return map;
	}
	
	private Element getTarget (String topName,String id){
		List<Element> list = getElementList(topName);
		for (Element ele : list){
			if (id.equals(ele.attribute("id").getValue())){
				return ele;
			}
		}
		return null;
	}
	
	/**
	 * 根据域上的注解{@link XMLParse}得到对象
	 * @param topName 转化的结点名称
	 * @param id 在所有符合条件的结果中属性id对应的值，用来唯一确定对象
	 * @param cls 期望生成的对象类型
	 * */
	public<T> T getBean (String topName,String id,Class<T> cls){
		
		Element ele = getTarget(topName, id);
		if (ele==null)return null;
		
		try {
			return getObject(ele, cls);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private <T> T getObject (Element ele,Class<T> cls) throws ClassNotFoundException{
		List<Field> list = getFitField(cls);
		BeanInjecter<T> inject = BeanInjecter.getInstance(cls);
		T t = null;
		try {
			t = cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (t==null)return null;
		
		for (Field f : list){
			XMLParse xp = f.getAnnotation(XMLParse.class);
			String key = xp.key().length()==0?f.getName():xp.key();
			if (f.getType().equals(List.class)){
				//集合类型
				List subList = new ArrayList();
				
				for (Element subEle:(List<Element>)ele.elements(key)){
					subList.add(getObject(subEle, Class.forName(xp.subType())));
				}
				inject.setBeanValue(t, f.getName(), subList);
			}else{
				inject.setBeanValue(t, f.getName(), getValue(ele,xp,key));
			}
		}
		return t;
	}
	
	/**
	 * 得到值
	 * */
	private String getValue(Element ele, XMLParse xp,String key) {
		
		switch (xp.type()){
		case Attribute:return ele.attributeValue(key);
		case SimpleElement:return ele.getText();//元素中间的内容
		case ComplexElement:
		default :return null;
		}
	}
	
	/**得到合适的域集合（即存在{@link XMLParse}的域）
	 * 
	 * */
	private List<Field> getFitField(Class cls){
		List<Field> list = ReflectTool.getAllField(cls);
		List<Field> targetList = new ArrayList<Field>();
		for (Field f : list){
			if (f.getAnnotation(XMLParse.class)!=null){
				targetList.add(f);
			}
		}
		return targetList;
	}
	
	public static void main(String[] args){
		XMLReader reader = new XMLReader("src/com/temobi/base/core/myconfig.xml");
		/*List<Element> list = reader.getElementList("subsystem");
		System.out.println(reader.getElemetAttribute(list.get(0)));*/
		SelectBean b = reader.getBean("select", "setterType", SelectBean.class);
		System.out.println(b.getId());
	}
}
