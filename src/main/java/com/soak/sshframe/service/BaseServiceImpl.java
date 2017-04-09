package com.soak.sshframe.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.soak.sshframe.action.BaseAction;
import com.soak.sshframe.dao.BaseDaoImpl;
import com.soak.sshframe.dao.IBaseDao;
import com.soak.sshframe.support.CustomExample;
import com.soak.sshframe.support.PaginationSupport;

//import com.jinf.framework.action.BaseAction;
//import com.jinf.framework.dao.BaseDaoImpl;
//import com.jinf.framework.dao.IBaseDao;
//import com.jinf.framework.support.CustomExample;
//import com.jinf.framework.support.PaginationSupport;

public class BaseServiceImpl<T> implements IBaseService<T> {

	protected static final long serialVersionUID = 1L;
	protected final Log log = LogFactory.getLog(BaseAction.class);
	protected IBaseDao<T> baseDAO;

	@Autowired(required = true)
	protected SessionFactory sessionFactory;

	public BaseServiceImpl() {

	}

	public BaseServiceImpl(SessionFactory sessionFactory, Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		BaseDaoImpl<T> temp = new BaseDaoImpl<T>(sessionFactory, entityClass);
		baseDAO = temp;
	}

	@SuppressWarnings("unchecked")
	public PaginationSupport<T> findByExample(CustomExample<T> example,
			int startIndex, int pageSize) {
		return (PaginationSupport) getDao().findPageByExample(example,
				startIndex, pageSize);
	}

	public T findById(Long id) {
		return getDao().findById(id);
	}

	public void save(T entity) {		
		getDao().save(entity);
	}

	public void delete(T entity) {
		getDao().delete(entity);
	}

	public void saveOrUpdate(T entity) {
		getDao().saveOrUpdate(entity);
	}

	public void update(T entity) {
		getDao().update(entity);
	}

	public void clear() {
		getDao().clear();
	}

	public void flush() {
		getDao().flush();
	}

	public void merge(T entity) {
		getDao().merge(entity);
	}

	public void persist(T entity) {
		getDao().persist(entity);
	}

	public void deleteAll(List<T> entities) {
		getDao().deleteAll(entities);
	}

	public List<T> findAllValid(Class<?> t, String field, Object validObject) {
		DetachedCriteria criterion = DetachedCriteria.forClass(t);
		criterion.add(Restrictions.eq(field, validObject));

		return getDao().findAllByCriteria(criterion);
	}
	
	public void saveOrUpdateAll(Collection<T> entities) {
		getDao().saveOrUpdateAll(entities);
	}

	public int deleteAllByProperties(Object... propertyNameAndValuePaires) {
		return getDao().deleteAllByProperties(propertyNameAndValuePaires);
	}

	public List<T> findAllByProperties(Object... propertyNameAndValuePaires) {
		return getDao().findAllByProperties(propertyNameAndValuePaires);
	}
	
	public List<T> findAllByOrLikeProperties(int max, Object... propertyNameAndValuePaires){
		return getDao().findAllByOrLikeProperties(max,propertyNameAndValuePaires);
	}

 	public  List<?> findDistinctObjectsByProperties(String[] distictPropertys,
			Object... propertyNameAndValuePaires) {
		return getDao().findDistinctObjectsByProperties(distictPropertys,
				propertyNameAndValuePaires);
	}

	// public <T2> BaseDaoImpl<T2> getBean(Class<T2> entityClass) {
	// BaseDaoImpl<T2> temp = new BaseDaoImpl<T2>(sessionFactory, entityClass);
	// // temp.setSessionFactory(sessionFactory);
	// // temp.setEntityClass(entityClass);
	// return temp;
	// }

	public IBaseDao<T> getDao() {
		return baseDAO;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> autoComplete(String table, String key, String column,
			Map parameters) {
		return getDao().autoComplete(table, key, column, parameters);
	}
	
	public List<T> findAll() {
		return getDao().findAll();
	}
	
	public List<T> findListById(Long parentId){
		return getDao().findListById(parentId);
	}

	public List findByHQL(String hql, Object[] value) {		
		return getDao().findByHQL(hql , value);
	}
}
