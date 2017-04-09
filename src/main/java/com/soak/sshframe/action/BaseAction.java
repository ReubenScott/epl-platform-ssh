package com.soak.sshframe.action;

import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//import com.jinf.constant.ResultCode;
//import com.jinf.constant.SystemContant;
//import com.jinf.diary.domain.DiaryComment;
//import com.jinf.framework.service.BaseServiceImpl;
//import com.jinf.framework.service.IBaseService;
//import com.jinf.framework.support.PaginationSupport;
//import com.jinf.system.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.soak.common.constant.ResultCode;
import com.soak.common.constant.SystemContant;
import com.soak.sshframe.service.BaseServiceImpl;
import com.soak.sshframe.service.IBaseService;
import com.soak.sshframe.support.PaginationSupport;
import com.soak.system.model.User;

@SuppressWarnings("serial")
@Conversion()
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>, Preparable, ServletContextAware  {
	
	protected User loginUser;
	protected final Log log = LogFactory.getLog(BaseAction.class);
	protected final int DEFAULT_PAGESIZE = 12;
	protected final String LIST = "list";
	protected final String EDIT = "edit";
	protected final String VIEW = "view";
	protected final String INDEX = "index";
	protected final String CONFIRM = "confirm";
	protected final String DELETE = "delete";
	protected Class<T> entityClass;
	protected T entity;
	protected IBaseService<T> service;
	protected Long id;
	protected int startIndex = 0;
	protected int pageSize = DEFAULT_PAGESIZE;
	protected PaginationSupport<T> listResult;
	protected ServletContext servletContext;
	public static WebApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public BaseAction() {
		
		TypeVariable[] typeVariables = getClass().getSuperclass().getTypeParameters();
		
		if (typeVariables != null && typeVariables.length > 0) {
			try {
				Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
				entityClass = (Class<T>)types[0];
				return;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public T getModel() {
		if (entity == null) {
			try {
				entity = (T) entityClass.newInstance();
			} catch (Exception e) {
				log.warn("无法创建模型实例");
				return null;
			}
		}
		return entity;
	}

	public void prepare() throws Exception {
		if (id == null || id < 1) {
			if (entity == null) {
				entity = (T) entityClass.newInstance();
				prepareModelInner(entity);
			}
		} else {
			entity = service.findById(id);
		}
		loginUser = (User)getSession().get(SystemContant.CURRENT_USER);
	}

	/**
	 * 模型新建时候的特殊处理，子类可以继承
	 * @param model
	 */
	protected void prepareModelInner(T model) {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public PaginationSupport<T> getListResult() {
		return listResult;
	}

	public void setListResult(PaginationSupport<T> listResult) {
		this.listResult = listResult;
	}

	public void setServletContext(ServletContext sc) {
		this.servletContext = sc;
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		BaseServiceImpl<T> temp = new BaseServiceImpl<T>((SessionFactory)applicationContext.getBean("sessionFactory"), entityClass);
		service = temp;
	}
	
	public void ajaxResponse(String message, ResultCode code, String objId) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml;charset=utf-8 ");
		PrintWriter out = null;
		try {
			String responseStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response code=\""+code.name()+"\" objId=\""+objId+"\">" + message + "</response>";
			out = response.getWriter();
			out.print(responseStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
	}
	
//	public void ajaxResponse(String message, String code, String objId, String objIdList) {
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setHeader("Cache-Control", "no-cache");
//		response.setContentType("text/xml;charset=utf-8 ");
//		PrintWriter out = null;
//		try {
//			String responseStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response code=\""+code+"\" message=\""+message+"\" objId=\""+objId+"\">" + objIdList + "</response>";
//			out = response.getWriter();
//			out.print(responseStr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null) {
//				try {
//					out.close();
//				} catch (Exception e) {
//
//				}
//			}
//		}
//	}
	
	public void responseXml(String xml) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml;charset=utf-8 ");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(xml);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected <T2> BaseServiceImpl<T2> getBean(Class<T2> entityClass) {
		BaseServiceImpl<T2> temp = new BaseServiceImpl<T2>((SessionFactory)applicationContext.getBean("sessionFactory"), entityClass);
		return temp;
	}
	
	protected Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}
	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	
}