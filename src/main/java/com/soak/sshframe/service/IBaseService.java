package com.soak.sshframe.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.soak.sshframe.support.PaginationSupport;


//import com.jinf.framework.dao.IBaseDao;
//import com.jinf.framework.support.CustomExample;
//import com.jinf.framework.support.PaginationSupport;
 
public interface IBaseService<T> extends java.io.Serializable {
	
	public void flush() ;
	
	public void save(T entity) ;

	public void delete(T entity) ;
	
	public void saveOrUpdate(T entity);

	public void update(T entity) ;
	
	public void clear();
	
	public void merge(T entity) ;
	
	public void persist(T entity);
	
	public void deleteAll(List<T> entities);
	
	public T findById(Long id);
	
	public List<T> findListById(Long parentId);
	
	public int deleteAllByProperties(Object... propertyNameAndValuePaires);
	 
	public List<T> findAllByProperties(Object... propertyNameAndValuePaires);
	
	public List<T> findAllByOrLikeProperties(int max,Object... propertyNameAndValuePaires);
	
	public List<?>  findDistinctObjectsByProperties(String[] distictPropertys, Object... propertyNameAndValuePaires);

//	public PaginationSupport<T> findByExample(CustomExample<T> example, int startIndex, int pageSize);

 	@SuppressWarnings("unchecked")
	public List<Object[]> autoComplete(String table, String key, String column,  Map parameters);
	//public void setSessionFactory(SessionFactory sessionFactory);
	
//	public IBaseDao<T> getDao();
	
	public List<T> findAll();
	
	public void saveOrUpdateAll(Collection<T> entities);
	
	public List  findByHQL(String hql , Object[] value);
}
