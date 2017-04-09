package com.soak.sshframe.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.ReplicationMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import com.soak.sshframe.support.CustomExample;
import com.soak.sshframe.support.PaginationSupport;


public interface IBaseDao<T> extends Serializable {

	public Class<T> getEntityClass();

	// ---------------------鍩烘湰鎿嶄綔------------------------
	public void save(T entity);

	public void update(T entity);

	public void delete(T entity);

	public void deleteAll(Collection<T> entities);
	
	public int deleteAllByProperties(Object... propertyNameAndValuePaires);

	public void evict(T entity);

	public void merge(T entity);

	public void persist(T entity);

	public void refresh(T entity);

	public void replicate(T entity, ReplicationMode replicationMode);

	public void saveAfterClear(T entity);

	public void saveOrUpdate(T entity);

	public void saveOrUpdateAfterClear(T entity);

	public void saveOrUpdateAll(Collection<T> entities);

	public void clear();

	public void flush();

	// ----------------------鏌ヨ--------------------------------------

	public T findById(Serializable id);

	public void excutSql(String sql);
	/**
	 *  
	 *  閫氳繃鏌ヨ parent_id  杩斿洖瀵瑰簲鎵�鏈夌殑涓嬩竴瀛愮骇 鑿滃崟
	 *  @return 
	 */
	public List<T> findListById(Long parentId);
	
	/**
	 * 妫�娴嬬粰鍑虹殑items鏄惁鍙寘鍚竴鏉￠鏈熸暟鎹�
	 * 
	 * @param items
	 * @return
	 */
	public T uniqueResult(List<T> items);

	/**
	 * 鏍规嵁鐢ㄦ埛鍒跺畾鐨勫叧鑱旀煡璇㈣繑鍥炵粺璁″��
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	public int countByCriteria(final DetachedCriteria detachedCriteria);

	public int countByExample(final CustomExample<T> example);

	public int countBySQL(final String sql);	

	public List<T> findAll();

	public List<T> findAllByCriteria(Criterion... criterion);

	public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria);

	public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria,
			final int firstResult, final int maxResults);

	public List<T> findAllByExample(final CustomExample<T> example);

	public List<T> findAllByExample(final CustomExample<T> example,
			final Order[] orders, final int firstResult, final int maxResults);

	public List<T> findAllByHQL(final String hql, final int firstResult,
			final int maxResults);

	public List<T> findAllByProperties(Object... propertyNameAndValuePaires);
	
	public List<T> findAllByOrLikeProperties(int max,Object... propertyNameAndValuePaires);
	
	public List<?>  findDistinctObjectsByProperties(String[] distictPropertys, Object... propertyNameAndValuePaires);
		
	// ---------------------鍒嗛〉--------------------------
	public PaginationSupport<T> findPage(final int startIndex,
			final int pageSize);

	public PaginationSupport<T> findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex,
			final int pageSize);

	public PaginationSupport<T> findPageByCriteria(
			final DetachedCriteria detachedCriteria, final Order[] orders,
			final int startIndex, final int pageSize);

	public PaginationSupport<T> findPageByExample(
			final CustomExample<T> example, final int startIndex,
			final int pageSize);

	public PaginationSupport<T> findPageByExample(
			final CustomExample<T> example, final Order[] orders,
			final int startIndex, final int pageSize);

 
	public PaginationSupport<Object[]> findPageBySQL(final String sql,
			final String[] scalar, final int startIndex, final int pageSize);
 	
	@SuppressWarnings("unchecked")
	public List<Object[]> autoComplete(String table, String key, String column, Map parameters);

	public List  findByHQL(String hql , Object... value);
}
