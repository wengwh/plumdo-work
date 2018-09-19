package com.plumdo.common.client.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.common.resource.PageResponse;

/**
 * JdbcTemplate做封装，统一打印日志，加入多参数等简化操作
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
public class JdbcClient {

	private Logger logger = LoggerFactory.getLogger(JdbcClient.class);

	private JdbcTemplate jdbcTemplate;

	public JdbcClient(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int update(String sql, QueryBulider queryBulider) {
		return update(getContactSql(sql, queryBulider), queryBulider.getArgsToArray());
	}

	public int update(String sql, List<Object> values, QueryBulider queryBulider) {
		values.addAll(queryBulider.getArgs());
		return update(getContactSql(sql, queryBulider), values.toArray());
	}

	public int update(String sql, Object... args) {
		logger.debug("执行SQL语句:{},参数:{}", sql, args);
		return jdbcTemplate.update(sql, args);
	}

	public Long insert(final String sql, final String pkey, final Object... args) {
		logger.debug("执行SQL语句:{},参数:{}", sql, args);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, new String[] { pkey });
				int i = 1;
				for (Object o : args) {
					ps.setObject(i++, o);
				}
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public int[] batchUpdate(String sql, final Object[] keys, final List<ObjectMap> entityList) {
		logger.debug("批量操作,SQL语句:{}", sql);
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
				ObjectMap map = entityList.get(i);
				List<Object> params = new ArrayList<>();
				for (int j = 0; j < keys.length; j++) {
					Object value = map.get(keys[j]);
					preparedStatement.setObject(j + 1, value);
					params.add(value);
				}
				logger.debug("批量操作,参数:{}", params);
			}

			@Override
			public int getBatchSize() {
				return entityList.size();
			}
		});
	}

	
	
	public void execute(String sql) {
		logger.debug("执行SQL语句:{}", sql);
		jdbcTemplate.execute(sql);
	}

	public ObjectMap queryForMap(String sql, ObjectMap paramMap) {
		return queryForMap(sql, new QueryBulider().allEq(paramMap));
	}

	public ObjectMap queryForMap(String sql, QueryBulider queryBulider) {
		return queryForMap(sql, queryBulider, "");
	}

	public ObjectMap queryForMap(String startSql, QueryBulider queryBulider, String endSql) {
		return queryForMap(getContactSql(startSql, queryBulider, endSql), queryBulider.getArgsToArray());
	}

	public ObjectMap queryForMap(String sql, Object... args) {
		logger.debug("执行SQL语句:{},参数:{}", sql, args);
		return jdbcTemplate.queryForObject(sql, new ObjectMapRowMapper(), args);
	}

	public List<ObjectMap> queryForList(String sql, ObjectMap paramMap) {
		return queryForList(sql, new QueryBulider().allEq(paramMap));
	}

	public List<ObjectMap> queryForList(String sql, QueryBulider queryBulider) {
		return queryForList(sql, queryBulider, "");
	}

	public List<ObjectMap> queryForList(String startSql, QueryBulider queryBulider, String endSql) {
		return queryForList(getContactSql(startSql, queryBulider, endSql), queryBulider.getArgsToArray());
	}

	public List<ObjectMap> queryForList(String sql, Object... args) {
		logger.debug("执行SQL语句:{},参数:{}", sql, args);
		return jdbcTemplate.query(sql, new ObjectMapRowMapper(), args);
	}

	public Long queryForCount(String sql, ObjectMap paramMap) {
		return queryForCount(sql, new QueryBulider().allEq(paramMap));
	}

	public Long queryForCount(String sql, QueryBulider queryBulider) {
		return queryForCount(sql, queryBulider, "");
	}

	public Long queryForCount(String startSql, QueryBulider queryBulider, String endSql) {
		return queryForCount(getContactSql(startSql, queryBulider, endSql), queryBulider.getArgsToArray());
	}

	public Long queryForCount(String sql, Object... args) {
		return queryForObject(sql, Long.class, args);
	}

	public <T> T queryForObject(String sql, QueryBulider queryBulider, Class<T> requiredType) {
		return queryForObject(sql, queryBulider, "", requiredType);
	}

	public <T> T queryForObject(String startSql, QueryBulider queryBulider, String endSql, Class<T> requiredType) {
		return queryForObject(getContactSql(startSql, queryBulider, endSql), requiredType,
				queryBulider.getArgsToArray());
	}

	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
		logger.debug("执行SQL语句:{},参数:{}", sql, args);
		return jdbcTemplate.queryForObject(sql.toString(), requiredType, args);
	}

	public PageResponse queryForPage(String sql, Pageable pageParam, QueryBulider queryBulider) {
		return queryForPage(sql, pageParam, queryBulider, "");
	}

	public PageResponse queryForPage(String startSql, Pageable pageParam, QueryBulider queryBulider, String endSql) {
		return queryForPage(getContactSql(startSql, queryBulider, endSql), pageParam, queryBulider.getArgsToArray());
	}

	public PageResponse queryForPage(String sql, Pageable pageable, Object... args) {
		PageResponse pageResponse = new PageResponse();

		StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT COUNT(1) AS TOTAL FROM (").append(sql).append(") temp ");

		pageResponse.setTotal(queryForCount(countSql.toString(), args));

		if (pageResponse.getTotal() <= 0) {
			pageResponse.setData(Collections.emptyList());
		} else {
			StringBuilder pageSql = new StringBuilder();
			pageSql.append("SELECT temp.* FROM (").append(sql).append(") temp ");

			if (pageable.getSort() != null) {
				pageSql.append(getOrderBySql(pageable.getSort()));
			}
			if (pageable.getPageNumber() > 0 && pageable.getPageSize() > 0) {
				pageSql.append(" LIMIT ").append(pageable.getOffset()).append(",").append(pageable.getPageSize());
			}
			pageResponse.setData(queryForList(pageSql.toString(), args));
		}
		return pageResponse;
	}

	private String getOrderBySql(Sort sort) {
		StringBuilder orderBy = new StringBuilder(" ORDER BY ");
		Iterator<Order> orders = sort.iterator();
		while (orders.hasNext()) {
			Order order = orders.next();
			orderBy.append("`").append(order.getProperty()).append("` ").append(order.getDirection().toString()).append(",");
		}
		orderBy.delete(orderBy.length() - 1, orderBy.length());
		return orderBy.toString();
	}

	private String getContactSql(String startSql, QueryBulider queryBulider) {
		return getContactSql(startSql, queryBulider, "");
	}

	private String getContactSql(String startSql, QueryBulider queryBulider, String endSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(startSql).append(queryBulider.getSql()).append(endSql);
		return sql.toString();
	}
}