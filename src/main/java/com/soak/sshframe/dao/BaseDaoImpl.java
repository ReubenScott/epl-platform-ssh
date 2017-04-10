package com.soak.sshframe.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.soak.sshframe.support.CustomExample;
import com.soak.sshframe.support.PaginationSupport;
import com.soak.system.model.Menu;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
  
  private static final long serialVersionUID = 1L;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private Class<T> entityClass;

  @Autowired(required = true)
  public void setMySessionFactory(SessionFactory sessionFactory) {
    super.setSessionFactory(sessionFactory);
  }

  public BaseDaoImpl() {
    try {
      entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public BaseDaoImpl(SessionFactory sessionFactory, Class<T> entityClass) {
    this.entityClass = entityClass;
    super.setSessionFactory(sessionFactory);
  }

  public Class<T> getEntityClass() {
    return entityClass;
  }

  // ------------------------基本操作------------------------
  public void save(T entity) {
    this.getHibernateTemplate().save(entity);
  }

  public void saveAfterClear(T entity) {
    this.getHibernateTemplate().clear();
    this.getHibernateTemplate().save(entity);
  }

  public void saveOrUpdate(T entity) {
    this.getHibernateTemplate().saveOrUpdate(entity);
  }

  public void saveOrUpdateAfterClear(T entity) {
    this.getHibernateTemplate().clear();
    this.getHibernateTemplate().saveOrUpdate(entity);
  }

  public void saveOrUpdateAll(Collection<T> entities) {
    this.getHibernateTemplate().saveOrUpdateAll(entities);
  }

  public void update(T entity) {
    this.getHibernateTemplate().update(entity);
  }

  public void delete(T entity) {
    this.getHibernateTemplate().delete(entity);
  }

  public void deleteAll(Collection<T> entities) {
    this.getHibernateTemplate().deleteAll(entities);
  }

  public void evict(T entity) {
    this.getHibernateTemplate().evict(entity);
  }

  public void merge(T entity) {
    this.getHibernateTemplate().merge(entity);
  }

  public void persist(T entity) {
    this.getHibernateTemplate().persist(entity);
  }

  public void refresh(T entity) {
    this.getHibernateTemplate().refresh(entity);
  }

  public void replicate(T entity, ReplicationMode replicationMode) {
    this.getHibernateTemplate().replicate(entity, replicationMode);
  }

  public void clear() {
    this.getHibernateTemplate().clear();
  }

  public void flush() {
    this.getHibernateTemplate().flush();
  }

  public int deleteAllByProperties(Object... propertyNameAndValuePaires) {
    List<T> list = this.findAllByProperties(propertyNameAndValuePaires);
    if (list != null && list.size() > 0) {
      deleteAll(list);
      return list.size();
    } else {
      return 0;
    }
  }

  // ------------------------查询------------------------

  public T findById(Serializable id) {
    return (T) this.getHibernateTemplate().get(entityClass, id);
  }

  public T uniqueResult(List<T> items) {
    if (items == null)
      return null;
    int size = items.size();

    switch (size) {
      case 1:
        return items.get(0);
      case 0:
        return null;
      default:
        logger.warn("预计获取一条数据，实际获取了多条");
        return null;
    }
  }

  public int countByCriteria(final DetachedCriteria detachedCriteria) {
    return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        int totalCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalCount;
      }
    });
  }

  public int countByExample(final CustomExample<T> example) {
    return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria executableCriteria = session.createCriteria(example.getEntityClass());

        executableCriteria.add(example);
        example.appendCondition(executableCriteria);

        int totalCount = ((Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalCount;
      }
    });
  }

  public int countBySQL(final String sql) {
    SQLQuery query = this.getSession().createSQLQuery(sql);
    query.addScalar("count");
    BigDecimal result = (BigDecimal) query.uniqueResult();
    return result.intValue();
  }

  public List<T> findAll() {
    return (List<T>) getSession().createCriteria(entityClass).list();
  }

  public List<T> findAllByCriteria(Criterion... criterion) {
    DetachedCriteria detachedCrit = DetachedCriteria.forClass(getEntityClass());
    for (Criterion c : criterion) {
      detachedCrit.add(c);
    }
    return (List<T>) getHibernateTemplate().findByCriteria(detachedCrit);
  }

  public List<T> findAllByProperties(Object... propertyNameAndValuePaires) {
    DetachedCriteria detachedCrit = DetachedCriteria.forClass(getEntityClass());
    int idx = 0;
    String propertyName = "";
    for (Object property : propertyNameAndValuePaires) {
      if (idx % 2 == 0) {
        propertyName = property.toString();
      }
      ;
      if (idx % 2 == 1) {
        detachedCrit.add(Restrictions.eq(propertyName, property));
      }
      ;
      idx++;
    }
    return (List<T>) getHibernateTemplate().findByCriteria(detachedCrit);
  }

  public List<?> findDistinctObjectsByProperties(String[] distictPropertys, Object... propertyNameAndValuePaires) {
    DetachedCriteria detachedCrit = DetachedCriteria.forClass(getEntityClass());
    int idx = 0;
    String propertyName = "";
    for (Object property : propertyNameAndValuePaires) {
      if (idx % 2 == 0) {
        propertyName = property.toString();
      }
      ;
      if (idx % 2 == 1) {
        detachedCrit.add(Restrictions.eq(propertyName, property));
      }
      ;
      idx++;
    }

    ProjectionList plist = Projections.projectionList();
    for (String pro : distictPropertys) {
      plist.add(Projections.property(pro.trim()));
    }
    detachedCrit.setProjection(Projections.distinct(plist));
    return getHibernateTemplate().findByCriteria(detachedCrit);
  }

  public List<T> findAllByOrLikeProperties(int max, Object... propertyNameAndValuePaires) {
    DetachedCriteria detachedCrit = DetachedCriteria.forClass(getEntityClass());
    Disjunction or = Restrictions.disjunction();
    int idx = 0;
    String propertyName = "";
    Property myProperty = null;
    for (Object property : propertyNameAndValuePaires) {
      if (idx % 2 == 0) {
        propertyName = property.toString();
        myProperty = Property.forName(propertyName.trim());
      }
      if (idx % 2 == 1) {
        or.add(myProperty.like(property.toString(), MatchMode.ANYWHERE));
      }
      idx++;
    }
    detachedCrit.add(or);
    getHibernateTemplate().setMaxResults(max);
    return getHibernateTemplate().findByCriteria(detachedCrit);
  }

  public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria) {
    return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return criteria.list();
      }
    });
  }

  public List<T> findAllByHQL(final String hql, final int firstResult, final int maxResults) {
    List<T> list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery(hql);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        List list = query.list();
        return list;
      }
    });
    return list;
  }

  public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria, final int firstResult, final int maxResults) {
    return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session).setFirstResult(firstResult).setMaxResults(maxResults);
        return criteria.list();
      }
    });
  }

  public List<T> findAllByExample(final CustomExample<T> example) {
    return findAllByExample(example, null, 0 , Integer.MAX_VALUE);
  }

  public List<T> findAllByExample(final CustomExample<T> example, final Order[] orders, final int firstResult, final int maxResults) {
    return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria executableCriteria = session.createCriteria(example.getEntityClass());

        executableCriteria.add(example);
        example.appendCondition(executableCriteria);

        executableCriteria.setProjection(null);
        executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        for (int i = 0; orders != null && i < orders.length; i++) {
          executableCriteria.addOrder(orders[i]);
        }
        List items = executableCriteria.setFirstResult(firstResult).setMaxResults(maxResults).list();
        return items;
      }
    });
  }

  // ------------------------分页查询------------------------
  public PaginationSupport<T> findPage(int startIndex, int pageSize) {
    return findPageByCriteria(DetachedCriteria.forClass(entityClass), startIndex, pageSize);
  }

  public PaginationSupport<T> findPageByCriteria(final DetachedCriteria detachedCriteria, final int startIndex, final int pageSize) {
    return findPageByCriteria(detachedCriteria, null, startIndex, pageSize);
  }

  public PaginationSupport<T> findPageByCriteria(final DetachedCriteria detachedCriteria, final Order[] orders, final int startIndex, final int pageSize) {
    return (PaginationSupport<T>) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);

        Integer integer = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult());

        int totalCount = 10;
        if (integer != null)
          totalCount = integer.intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        for (int i = 0; orders != null && i < orders.length; i++) {
          criteria.addOrder(orders[i]);
        }

        List items = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();

        PaginationSupport ps = new PaginationSupport(items, totalCount, startIndex, pageSize);
        return ps;
      }
    });
  }

  public PaginationSupport<T> findPageByExample(final CustomExample<T> example, final int startIndex, final int pageSize) {
    return findPageByExample(example, null, startIndex, pageSize);
  }

  public PaginationSupport<T> findPageByExample(final CustomExample<T> example, final Order[] orders, final int startIndex, final int pageSize) {

    HibernateCallback hcb = new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        Criteria executableCriteria = session.createCriteria(example.getEntityClass());

        executableCriteria.add(example);
        example.appendCondition(executableCriteria);
        Integer c = (Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
        int totalCount = 0;
        if (c != null)
          totalCount = c.intValue();

        executableCriteria.setProjection(null);
        executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        for (int i = 0; orders != null && i < orders.length; i++) {
          executableCriteria.addOrder(orders[i]);
        }

        List items = executableCriteria.setFirstResult(startIndex).setMaxResults(pageSize).list();

        PaginationSupport ps = new PaginationSupport(items, totalCount, startIndex, pageSize);
        return ps;
      }
    };
    HibernateTemplate ht = this.getHibernateTemplate();
    return (PaginationSupport) ht.execute(hcb);
  }

  public PaginationSupport<Object[]> findPageBySQL(final String sql, final String[] scalar, final int startIndex, final int pageSize) {
    SQLQuery query = this.getSession().createSQLQuery(sql);
    query.setFirstResult(startIndex);
    query.setMaxResults(pageSize);
    for (String s : scalar)
      query.addScalar(s);
    List<Object> list = query.list();

    query = this.getSession().createSQLQuery("select count(*) as count " + sql.substring(sql.indexOf("from")));
    query.addScalar("count");
    BigDecimal result = (BigDecimal) query.uniqueResult();

    PaginationSupport resultList = new PaginationSupport(list, (result != null) ? result.intValue() : 0, startIndex, pageSize);
    return resultList;
  }

  public List<Object[]> autoComplete(String table, String keyWord, String column, Map parameters) {
    StringBuffer sql = new StringBuffer("select id, name from ");
    sql.append(table);
    // sql.append(" where validflag = 'VALID' and ");
    sql.append(" where ");
    sql.append(column);
    sql.append(" like '%");
    sql.append(keyWord);
    sql.append("%'");
    if (parameters != null) {
      Iterator keys = parameters.keySet().iterator();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        if (!key.equals("q") && !key.equals("table") && !key.equals("keyLabel") && !key.equals("limit") && !key.equals("timestamp")) {
          // String[] value = (String[]) parameters.get(key);
          String value = (String) parameters.get(key);
          sql.append(" and ");
          sql.append(key);
          sql.append("='");
          // sql.append(value[0]);
          sql.append(value);
          sql.append("'");
        }
      }
    }
    System.out.println("------------------------------------------- sql:" + sql.toString());
    SQLQuery query = this.getSession().createSQLQuery(sql.toString());
    query.setMaxResults(20);
    query.addScalar("id");
    query.addScalar("name");
    return query.list();
  }

  /**
   * 通过查询 parent_id 返回对应所有的下一子级 菜单
   * 2010.11.30
   */

  public List<T> findListById(Long parentId) {
    DetachedCriteria criteria = DetachedCriteria.forClass(Menu.class);
    criteria.add(Restrictions.ge("parent_id", parentId));
    return getHibernateTemplate().findByCriteria(criteria);
  }

  public void excutSql(String sql) {
    Session session = getSessionFactory().openSession();
    SQLQuery org = session.createSQLQuery(sql);
    org.executeUpdate();
    session.close();
  }

  public List findByHQL(String hql, Object... value) {
    return getHibernateTemplate().find(hql, value);
  }
  
}
