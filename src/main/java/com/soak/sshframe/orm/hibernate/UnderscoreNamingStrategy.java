package com.soak.sshframe.orm.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

/**
 * 数据库 表名称、 字段名称  命名策略
 */
public class UnderscoreNamingStrategy extends ImprovedNamingStrategy implements NamingStrategy {
  private static final long serialVersionUID = 1L;

  @Override
  public String classToTableName(String className) {
    return "T_" + tableName(className);
  }

  @Override
  public String propertyToColumnName(String propertyName) {
    return addUnderscores(propertyName).toUpperCase();
  }

  @Override
  public String columnName(String columnName) {
    return addUnderscores(columnName).toUpperCase();
  }

  @Override
  public String tableName(String tableName) {
    return addUnderscores(tableName).toUpperCase();
  }

}
