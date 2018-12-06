package com.plumdo.common.client.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.plumdo.common.model.ObjectMap;

/**
 * JDBC结果转换类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ObjectMapRowMapper implements RowMapper<ObjectMap> {

    @Override
    public ObjectMap mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        ObjectMap mapOfColValues = new ObjectMap();
        for (int i = 1; i <= columnCount; i++) {
            String key = JdbcUtils.lookupColumnName(rsmd, i);
            Object obj = JdbcUtils.getResultSetValue(rs, i, String.class);
            mapOfColValues.put(key, obj);
        }
        return mapOfColValues;
    }

}