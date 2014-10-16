package com.hqch.simple.resource.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hqch.simple.exception.DataBaseException;
import com.hqch.simple.util.JDBCUtil;


public abstract class BaseDAO {
	
	/**
	 * Return the connection which shared by current thread.
	 * 
	 * @return the connection which shared by current thread. * @throws DataStoreException */
	public abstract Connection getConnection();
	
	//
	protected static final ResultSetRowHandler<Integer> INT_ROW_HANDLER = 
		new ResultSetRowHandler<Integer>() {
		@Override
		public Integer handleRow(ResultSetRow row) throws Exception {
			return row.getInt(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Double> DOUBLE_ROW_HANDLER = 
		new ResultSetRowHandler<Double>() {
		@Override
		public Double handleRow(ResultSetRow row) throws Exception {
			return row.getDouble(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Float> FLOAT_ROW_HANDLER = 
		new ResultSetRowHandler<Float>() {
		@Override
		public Float handleRow(ResultSetRow row) throws Exception {
			return row.getFloat(1);
		}
	};
	//
	protected static final ResultSetRowHandler<BigDecimal> BIGDECIMAL_ROW_HANDLER = 
		new ResultSetRowHandler<BigDecimal>() {
		@Override
		public BigDecimal handleRow(ResultSetRow row) throws Exception {
			return row.getBigDecimal(1);
		}
	};
	//
	protected static final ResultSetRowHandler<String> STRING_ROW_HANDLER = 
		new ResultSetRowHandler<String>() {
		@Override
		public String handleRow(ResultSetRow row) throws Exception {
			return row.getString(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Byte> BYTE_ROW_HANDLER = 
		new ResultSetRowHandler<Byte>() {
		@Override
		public Byte handleRow(ResultSetRow row) throws Exception {
			return row.getByte(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Short> SHORT_ROW_HANDLER = 
		new ResultSetRowHandler<Short>() {
		@Override
		public Short handleRow(ResultSetRow row) throws Exception {
			return row.getShort(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Boolean> BOOLEAN_ROW_HANDLER = 
		new ResultSetRowHandler<Boolean>() {
		@Override
		public Boolean handleRow(ResultSetRow row) throws Exception {
			return row.getBoolean(1);
		}
	};
	//
	protected static final ResultSetRowHandler<Date> DATE_ROW_HANDLER = 
		new ResultSetRowHandler<Date>() {
		@Override
		public Date handleRow(ResultSetRow row) throws Exception {
			return row.getDate(1);
		}
	};

	// --------------------------------------------------------------------------
	/**
	
	 * 
	 * @param sql String
	 * @param rowHandler ResultSetRowHandler<T>
	 * @param parameters Object[]
	 * @return T
	 */
	protected final <T> T queryForObject(
			String sql,
			ResultSetRowHandler<T> rowHandler, 
			Object... parameters) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtil.set(conn, ps, parameters);
			rs = ps.executeQuery();
			if (rs.next()) {
				ResultSetRow row = new ResultSetRow(rs);
				return rowHandler.handleRow(row);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}

	//
	/**
	 * Method queryForList.
	 * @param sql String
	 * @param rowHandler ResultSetRowHandler<T>
	 * @param parameters Object[]
	 * @return List<T>
	 */
	protected final <T> List<T> queryForList(
			String sql,
			ResultSetRowHandler<T> rowHandler, 
			Object... parameters) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtil.set(conn, ps, parameters);
			rs = ps.executeQuery();
			List<T> result = new ArrayList<T>();
			while (rs.next()) {
				ResultSetRow row = new ResultSetRow(rs);
				result.add(rowHandler.handleRow(row));
			}
			return result;
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}
	//
	/**
	 * Method queryForBoolean.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Boolean
	 */
	protected final Boolean queryForBoolean(String sql,Object ...parameters){
		return  queryForObject(sql, BOOLEAN_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForString.
	 * @param sql String
	 * @param parameters Object[]
	 * @return String
	 */
	protected final String queryForString(String sql,Object ...parameters){
		return  queryForObject(sql, STRING_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForDouble.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Double
	 */
	protected final Double queryForDouble(String sql,Object ...parameters){
		return  queryForObject(sql, DOUBLE_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForFloat.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Float
	 */
	protected final Float queryForFloat(String sql,Object ...parameters){
		return  queryForObject(sql, FLOAT_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForInteger.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Integer
	 */
	protected final Integer queryForInteger(String sql,Object ...parameters){
		return  queryForObject(sql, INT_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForShort.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Short
	 */
	protected final Short queryForShort(String sql,Object ...parameters){
		return  queryForObject(sql, SHORT_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForBigDecimal.
	 * @param sql String
	 * @param parameters Object[]
	 * @return BigDecimal
	 */
	protected final BigDecimal queryForBigDecimal(String sql,Object ...parameters){
		return  queryForObject(sql, BIGDECIMAL_ROW_HANDLER, parameters);
	}
	/**
	 * Method queryForByte.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Byte
	 */
	protected final Byte queryForByte(String sql,Object ...parameters){
		return  queryForObject(sql, BYTE_ROW_HANDLER, parameters);
	}
	
	/**
	 * Method queryForDate.
	 * @param sql String
	 * @param parameters Object[]
	 * @return Date
	 */
	protected final  Date queryForDate(String sql,Object ...parameters){
		return queryForObject(sql, DATE_ROW_HANDLER, parameters);
	}
	//--------------------------------------------------------------------------
	/**
	 * @param sql String
	 * @param parameters Object[]
	 * @return boolean
	 */
	protected final boolean execute(
			String sql,
			Object... parameters) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtil.set(conn, ps, parameters);
			return ps.execute();
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}
	//
	/**
	 * @param sql String
	 * @param parameters Object[]
	 * @return boolean
	 */
	protected final int executeWithGenKey(
			String sql,
			Object... parameters) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			JDBCUtil.set(conn, ps, parameters);
			ps.execute();
			rs=ps.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}else{
				throw new DataBaseException("can not got generatedKeys");
			}
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}
	//
	protected final int executeWithGenKey(String sql) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute(sql,Statement.RETURN_GENERATED_KEYS);
			rs=ps.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}else{
				throw new DataBaseException("can not got generatedKeys");
			}
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	
	}
	//
	/**
	 * @param sql String
	 * @param parameters Object[]
	 * @return int
	 */
	protected final int executeUpdate(
			String sql,
			Object... parameters) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtil.set(conn, ps, parameters);
			return ps.executeUpdate();
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}
	//
	/**
	 * Method executeBatch.
	 * @param sql String
	 * @param parameterList List<Object[]>
	 * @return int[]
	 */
	protected final int[] executeBatch(
			String sql,
			List<Object[]> parameterList) {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for(Object []parameters:parameterList){
				JDBCUtil.set(conn, ps, parameters);
				ps.addBatch();
			}
			return ps.executeBatch();
		} catch (Exception e) {
			throw new DataBaseException(e);
		} finally {
			JDBCUtil.closeStatement(ps);
			JDBCUtil.closeConnection(conn);
		}
	}
}
