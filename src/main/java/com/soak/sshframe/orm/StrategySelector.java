package com.soak.sshframe.orm;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
//import com.soak.base.anno.StrategySupport;
//import com.soak.base.core.reflect.BeanBuilder;
//import com.soak.base.core.reflect.ReflectTool;
//import com.soak.base.model.OptionBean;
//import com.soak.base.model.SelectBean;
//import com.soak.base.util.XMLReader;
import com.soak.sshframe.annotation.StrategySupport;
import com.soak.sshframe.model.OptionBean;
import com.soak.sshframe.model.SelectBean;
import com.soak.sshframe.util.XMLReader;

/**
 * 用于实现策略选择器<br>
 * 注意：动态策略中使用了spring的 api
 * */
public class StrategySelector {

	private static Log log = LogFactory.getLog(StrategySelector.class);
	
	/**
	 * 获得策略选择器创建的一个 实例，需要目标Class支持StrategySupport注解
	 * */
	public static <T> T getInstance (Class<T> cls,Object... args){
		
		Class<? extends T> runtimeCls = getRuntimeCls(cls);
		
			return null;
		
//		T t = BeanBuilder.createBean(runtimeCls, args);
//		log.debug("class <"+runtimeCls.getName()+"> has created a instance for the strategy of target class <"+cls.getName()+">");
//		return t;
	}
	
	/**
	 * 得到运行时Class
	 * */
	public static <T> Class<? extends T> getRuntimeClass (Class<T> cls){
		return getRuntimeCls(cls);
	}
	
	
	private static <T> Class<? extends T> getRuntimeCls(Class<T> cls){
		//加载策略支持注解
		StrategySupport ss = cls.getAnnotation(StrategySupport.class);
		if (ss==null){
			log.debug("The target class <"+cls.getName()+"> must use annotation StrategySupport.");
			return null;
		}
		//这里要使用WEB的相对路径，由于启动时不存在ServletContext相关信息，故使用ClassLoader加载
		InputStream is=StrategySelector.class.getClassLoader().getResourceAsStream(
				ss.configPath());
		//根据输入流得到XML解析器
		if (is==null){
			log.debug("The file path <"+ss.configPath()+"> has not exist.Please check the config.");
			return null;
		}
		XMLReader reader = new XMLReader(is);
		//根据指定的配置文件得到当前选择的策略
		SelectBean sb = reader.getBean("select", ss.id(), SelectBean.class);
		
		//使用配置策略来生成策略执行对象
		String clsName = getRuntimeCls(sb);
		if (clsName==null){
			log.debug("System can't choose any strategy.Please check the config.");
			return null;
		}
		Class<T> runtimeCls = null;
		try {
			runtimeCls = (Class<T>)Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (runtimeCls==null){
			log.debug("The class <"+clsName+"> can't be loaded.");
			return null;
		}
		return runtimeCls;
	}
	
	/**
	 * 从配置文件Bean中得到配置的class名称
	 * */
	private static String getRuntimeCls(SelectBean sb){
		if (sb.getSelectType().equals("static")){
			for(OptionBean ob : sb.getOptionList()){
				if (ob.isSelected()){
					return ob.getCls();
				}
			}
			//没有选择属性默认选择第一个
			return sb.getOptionList().size()>0?sb.getOptionList().get(0).getCls():null;
		}else if (sb.getSelectType().equals("dynamic")){
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
			Object service = ac.getBean(sb.getServiceId());
			Method m = null;
			try {
				m = service.getClass().getMethod(sb.getMethod());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			if (m!=null){
				try {
					return m.invoke(service).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}else
		{
			return null;
		}
	}
}
