package com.plumdo.common.client.jdbc;

import java.util.*;


/**
 * 查询条件构造器
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class QueryBulider {
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String AND_NEW = "AND (";
    private static final String START = "(";
    private static final String END = ") ";

    private List<QueryParams> allQueryObjects = new ArrayList<>();
    private QueryParams currentQueryObject = null;

    private String sql = null;
    private List<Object> args = null;
    private boolean isBuild = false;

    public QueryBulider() {
        currentQueryObject = new QueryParams(this);
        allQueryObjects.add(currentQueryObject);
    }

    public QueryBulider or() {
        if (currentQueryObject.inOrStatement) {
            throw new RuntimeException("the query is already in an or statement");
        }
        currentQueryObject = new QueryParams(this, true);
        allQueryObjects.add(currentQueryObject);
        return this;
    }

    public QueryBulider endOr() {
        if (!currentQueryObject.inOrStatement) {
            throw new RuntimeException("endOr() can only be called after calling or()");
        }
        currentQueryObject = new QueryParams(this);
        allQueryObjects.add(currentQueryObject);
        return this;
    }

    public QueryBulider remove() {
        currentQueryObject.remove(currentQueryObject.queryParams.size() - 1);
        return this;
    }

    public QueryBulider where(QueryParam queryParam) {
        currentQueryObject.add(queryParam);
        return this;
    }

    public QueryBulider where(String column, Object value, QueryType queryType, boolean isNeed) {
        currentQueryObject.add(new QueryParam(column, value, queryType, isNeed));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field=value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider eq(String column, Object value) {
        return eq(column, value, false);
    }

    /**
     * <p>
     * 等同于SQL的"field=value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @param isNeed
     * @return this
     */
    public QueryBulider eq(String column, Object value, boolean isNeed) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.EQ, isNeed));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field=field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider eqColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.EQ_COLUMN));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field=value"表达式
     * </p>
     *
     * @param values
     * @return this
     */
    public QueryBulider allEq(Map<String, Object> values) {
        return allEq(values, false);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public QueryBulider allEq(Map<String, Object> values, boolean isNeed) {
        if (values != null) {
            Iterator iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                currentQueryObject.add(new QueryParam(entry.getKey(), entry.getValue(), QueryType.EQ, isNeed));
            }

        }
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<>value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider notEq(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.NOT_EQ));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<>field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider notEqColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.NOT_EQ_COLUMN));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field>value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider gt(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.GT));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field>field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider gtColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.GT_COLUMN));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field>=value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider ge(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.GE));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field>=field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider geColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.GE_COLUMN));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider lt(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.LT));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider ltColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.LT_COLUMN));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<=value"表达式
     * </p>
     *
     * @param column
     * @param value
     * @return this
     */
    public QueryBulider le(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.LE));
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<=field"表达式
     * </p>
     *
     * @param column
     * @param compareColumn
     * @return this
     */
    public QueryBulider leColumn(String column, Object compareColumn) {
        currentQueryObject.add(new QueryParam(column, compareColumn, QueryType.LE_COLUMN));
        return this;
    }

    /**
     * LIKE条件语句，value中前后%
     *
     * @param column 字段名称
     * @param value  匹配值
     * @return this
     */
    public QueryBulider like(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.LIKE));
        return this;
    }

    /**
     * LIKE条件语句，value中前%
     *
     * @param column 字段名称
     * @param value  匹配值
     * @return this
     */
    public QueryBulider likeLeft(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.LIKE_LEFT));
        return this;
    }

    /**
     * LIKE条件语句，value中后%
     *
     * @param column 字段名称
     * @param value  匹配值
     * @return this
     */
    public QueryBulider likeRight(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.LIKE_RIGHT));
        return this;
    }

    /**
     * is not null 条件
     *
     * @param column 字段名称。多个字段以逗号分隔。
     * @return this
     */
    public QueryBulider isNotNull(String column) {
        currentQueryObject.add(new QueryParam(column, QueryType.NOT_NULL));
        return this;
    }

    /**
     * is null 条件
     *
     * @param column 字段名称。多个字段以逗号分隔。
     * @return this
     */
    public QueryBulider isNull(String column) {
        currentQueryObject.add(new QueryParam(column, QueryType.NULL));
        return this;
    }

    /**
     * IN 条件语句
     *
     * @param column 字段名称
     * @param value  逗号拼接的字符串
     * @return this
     */
    public QueryBulider in(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.IN));
        return this;
    }


    /**
     * NOT IN条件语句
     *
     * @param column 字段名称
     * @param value  逗号拼接的字符串
     * @return this
     */
    public QueryBulider notIn(String column, Object value) {
        currentQueryObject.add(new QueryParam(column, value, QueryType.NOT_IN));
        return this;
    }

    /**
     * betwwee 条件语句
     *
     * @param column 字段名称
     * @param val1
     * @param val2
     * @return this
     */
    public QueryBulider between(String column, String val1, String val2) {
        List<String> value = new ArrayList<>();
        value.add(val1);
        value.add(val2);
        currentQueryObject.add(new QueryParam(column, value, QueryType.BETWEEN));
        return this;
    }

    /**
     * 两个条件AND语句
     *
     * @param column1 字段名称1
     * @param val1
     * @param column2 字段名称2
     * @param val2
     * @return this
     */
    public QueryBulider eqAnd(String column1, Object val1, String column2, Object val2) {
        List<Object> value = new ArrayList<>();
        value.add(val1);
        value.add(val2);
        currentQueryObject.add(new QueryParam(column1 + ";" + column2, value, QueryType.EQ_AND));
        return this;
    }

    public String getSql() {
        if (!isBuild) {
            buildQueryBuilder();
        }
        return sql;
    }

    public List<Object> getArgs() {
        if (!isBuild) {
            buildQueryBuilder();
        }
        return args;
    }

    public Object[] getArgsToArray() {
        return getArgs().toArray();
    }

    public QueryBulider clear() {
        allQueryObjects.clear();
        currentQueryObject = new QueryParams(this);
        allQueryObjects.add(currentQueryObject);
        refresh();
        return this;
    }

    private void refresh() {
        this.sql = null;
        this.args = null;
        this.isBuild = false;
    }

    private void buildQueryBuilder() {
        List<Object> values = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder(" ");
        String join = null;
        for (QueryParams params : allQueryObjects) {
            join = params.inOrStatement ? AND_NEW : AND;
            for (QueryParam queryParam : params.queryParams) {
                if (queryParam.isNeedContains()) {
                    sqlArgsFill(sbSql, values, queryParam, join);
                    if (params.inOrStatement) {
                        join = OR;
                    }
                }
            }
            if (params.inOrStatement && !AND_NEW.equals(join)) {
                sbSql.append(END);
            }
        }

        this.sql = sbSql.toString();
        this.args = values;
        this.isBuild = true;
    }

    @SuppressWarnings("unchecked")
    private void sqlArgsFill(StringBuilder sql, List<Object> values, QueryParam queryParam, String join) {
        String column = queryParam.getColumn();
        Object value = queryParam.getValue();
        QueryType queryType = queryParam.getType();
        switch (queryType) {
            case EQ:
            case NOT_EQ:
            case GT:
            case LT:
            case GE:
            case LE:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ? ");
                values.add(value);
                break;
            case EQ_COLUMN:
            case NOT_EQ_COLUMN:
            case GT_COLUMN:
            case LT_COLUMN:
            case GE_COLUMN:
            case LE_COLUMN:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(value).append(" ");
                break;
            case LIKE:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ? ");
                values.add("%" + value + "%");
                break;
            case LIKE_LEFT:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ? ");
                values.add("%" + value);
                break;
            case LIKE_RIGHT:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ? ");
                values.add(value + "%");
                break;
            case NULL:
            case NOT_NULL:
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ");
                break;
            case IN:
            case NOT_IN:
                if (value instanceof Collection) {
                    List<Object> list = (List<Object>) value;
                    sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" ( ");
                    for (Object item : list) {
                        sql.append("?,");
                        values.add(item);
                    }
                    sql.replace(sql.length() - 1, sql.length(), ") ");
                } else {
                    sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(" (").append(value).append(") ");
                }
                break;
            case BETWEEN:
                List<Object> list = (List<Object>) value;
                sql.append(join).append(" ").append(column).append(" ").append(queryType.getKey()).append(list.get(0)).append(" AND ").append(list.get(1)).append(" ");
                break;
            case EQ_AND:
                String[] columns = column.split(";");
                sql.append(join).append(START).append(columns[0]).append(" ").append(queryType.getKey()).append(" ? ")
                        .append(AND).append(" ").append(columns[1]).append(" ").append(queryType.getKey()).append(" ? ").append(END);
                values.addAll((List<Object>) value);
                break;
            default:
                break;
        }
    }

    private class QueryParams {
        private List<QueryParam> queryParams = new ArrayList<>();
        private boolean inOrStatement = false;
        private QueryBulider queryBulider = null;

        public QueryParams(QueryBulider queryBulider) {
            this.queryBulider = queryBulider;
        }

        public QueryParams(QueryBulider queryBulider, boolean inOrStatement) {
            this.queryBulider = queryBulider;
            this.inOrStatement = inOrStatement;
        }

        public void add(QueryParam queryParam) {
            queryParams.add(queryParam);
            queryBulider.refresh();
        }

        public void remove(int index) {
            queryParams.remove(index);
            queryBulider.refresh();
        }

    }
}