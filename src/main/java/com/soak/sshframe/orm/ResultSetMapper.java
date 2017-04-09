package com.soak.sshframe.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 支持的数据库值映射
 * */
public enum ResultSetMapper {

	/**
	 * 逻辑类型
	 * */
	BooleanType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == java.lang.Boolean.class || cls == java.lang.Boolean.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getBoolean(label);
		}
	},ByteType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Byte.class || cls == Byte.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException{
			return result.getByte(label);
		}
	},CharType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Character.class || cls == Character.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getInt(label);
		}
	},ShortType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Short.class || cls == Short.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getShort(label);
		}
	},
	IntegerType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Integer.class || cls == Integer.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getInt(label);
		}
	}
	,LongType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Long.class || cls == Long.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getLong(label);
		}
		
	},FloatType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Float.class || cls == Float.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getFloat(label);
		}
	},
	DoubleType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == Double.class || cls == Double.TYPE;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getDouble(label);
		}
	},DateType{
		
		@Override
		public boolean isMatched(Class cls) {
			return cls == java.util.Date.class || cls.getSuperclass()==java.util.Date.class;
		}
		
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getDate(label);
		}
		
	},StringType{
		@Override
		public boolean isMatched(Class cls) {
			return cls == String.class;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getString(label);
		}
	},MathType{
		@Override
		protected Object getValue(ResultSet result, String label)
				throws SQLException {
			return result.getBigDecimal(label);
		}
		@Override
		protected boolean isMatched(Class cls) {
			return cls == java.math.BigDecimal.class;
		}
	},
	/**其他类型*/
	EtcType{
		@Override
		public boolean isMatched(Class cls) {
			return true;
		}
		@Override
		public Object getValue(ResultSet result, String label) throws SQLException {
			return result.getObject(label);
		}
	};
	
	private static Log log = LogFactory.getLog(ResultSetMapper.class);
	
	/**
	 * 得到数据集对应数据
	 * 如果发生了任何错误，返回null
	 * */
	public Object getResultSetData(ResultSet result,String label){
		try {
			return getValue(result, label);
		}catch (SQLException e){
			//e.printStackTrace();
			log.debug(e);
		}
		return null;
	}
	
	/**
	 * 检查一个类型是否匹配当前的数据类型映射
	 * */
	protected abstract boolean isMatched (Class cls);
	/**
	 * 得到相应数据类型的值
	 * */
	protected abstract Object getValue (ResultSet result ,String label) throws SQLException;
	
	/**
	 * 得到一个匹配的数据映射
	 * */
	public static ResultSetMapper matchValueMapper(Class cls){
		for (ResultSetMapper mapper :ResultSetMapper.values()){
			if (mapper.isMatched(cls)){
				return mapper;
			}
		}
		throw new RuntimeException("Unsupported class <"+cls.getName()+"> has requested mapper.");
	}
}
