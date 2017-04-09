package com.soak.sshframe.support;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

public class CustomExample<T> extends Example {
	
	private static final long serialVersionUID = -5311571786646576498L;
	
	protected final Log log = LogFactory.getLog(CustomExample.class);
	protected T entity;
	protected Class<T> entityClass;
	protected List<BetweenDateProperty> betweenDatePropertyList;

	public static PropertySelector NoVale = new NoValuePropertySelector();

	public CustomExample(T obj) {
		this(obj, null, NoVale);
	}

	public CustomExample(T obj, Class<T> entityClass) {
		this(obj, entityClass, NoVale);
	}

	public CustomExample(T entity, PropertySelector selector) {
		this(entity, null, selector);
	}

	@SuppressWarnings("unchecked")
	public CustomExample(T entity, Class<T> entityClass,PropertySelector selector) {
		super(entity, selector == null ? NoVale : selector);

		this.entity = entity;

		if (entityClass == null) {
			try {
				this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			} catch (Exception ex) {
				log.warn("无法获取CustomExample的类型，确认创建CustomExample的时候是使用子类方式", ex);
			}
		} else {
			this.entityClass = entityClass;
		}
	}
	
	/**
	 * 增加日期范围查询条件
	 * @param name
	 * @param startDate
	 * @param endDate
	 */
	public void addBetweenDateProperty(String name, Date startDate, Date endDate) {
		if (name != null && name.trim().length() > 0) {
			if (startDate == null && endDate == null) {
				return;
			} else {
				if (betweenDatePropertyList == null) {
					betweenDatePropertyList = new ArrayList<BetweenDateProperty>();
				}
				betweenDatePropertyList.add(new BetweenDateProperty(name,startDate, endDate));
			}
		}
	}

	/**
	 * 增加日期范围查询条件
	 * @param criteria
	 */
	public void appendBetweenDateProperty(Criteria criteria) {
		if (betweenDatePropertyList != null) {
			for (BetweenDateProperty property : betweenDatePropertyList) {
				criteria.add(Restrictions.between(property.getName(), property.getStartDate(), property.getEndDate()));
			}
		}
	}
	
	/**
	 * 可以添加各种附加条件，比较常用的是添加对引用对象的值判断
	 * 
	 * @param criteria
	 */
	public void appendCondition(Criteria criteria) {

	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
}

class NoValuePropertySelector implements PropertySelector {
	private static final long serialVersionUID = -2869026741463677275L;

	public boolean include(Object object, String propertyName, Type type) {
		boolean ret = object != null && (!(object instanceof Number) || ((Number) object).longValue() != 0);
		return ret && !"".equals(object);
	}

	private Object readResolve() {
		return CustomExample.NoVale;
	}
}


class BetweenDateProperty {
	private String name;
	private Date startDate;
	private Date endDate;

	public BetweenDateProperty(String name, Date startDate, Date endDate) {
		this.name = name.trim();
		if (startDate == null) 
			this.startDate = CalendarUtil.getVirtualStartOfFirstDay();
		else 
			this.startDate = CalendarUtil.getBeginOfDay(startDate);
		
		if (endDate == null) 
			this.endDate = CalendarUtil.getEndOfToday();
		else
			this.endDate = CalendarUtil.getEndOfDay(endDate);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

}

class CalendarUtil {
	/**
	 * 获取虚拟的第一天
	 */
	public static Date getVirtualStartOfFirstDay() {
		Calendar calendar = new GregorianCalendar(1978, 11, 30);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	};

	/**
	 * 获取今天的结束
	 */
	public static Date getEndOfToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	};

	/**
	 * 获取一天的结束
	 * 
	 * @param date
	 * @return 23:59:59.999
	 */
	public static Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	};
	
	/**
	 * 获取一天的开始
	 * 
	 * @param date
	 * @return 00:00:00.000
	 */
	public static Date getBeginOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	};
}