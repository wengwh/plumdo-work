package com.plumdo.common.jpa;

import com.plumdo.common.jpa.Criterion.Operator;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 条件构造器 用于创建条件表达式
 *
 * @author wengwenhui
 * @date 2018年3月29日
 */
public class Restrictions {

    public static SimpleExpression eq(String fieldName, Object value) {
        return eq(fieldName, value, true);
    }

    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.EQ);
    }

    public static SimpleExpression notEq(String fieldName, Object value) {
        return notEq(fieldName, value, true);
    }

    public static SimpleExpression notEq(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.NE);
    }

    public static SimpleExpression like(String fieldName, String value) {
        return like(fieldName, value, true);
    }

    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.LIKE);
    }

    public static SimpleExpression likeLeft(String fieldName, String value) {
        return likeLeft(fieldName, value, true);
    }

    public static SimpleExpression likeLeft(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.LIKE_LEFT);
    }

    public static SimpleExpression likeRight(String fieldName, String value) {
        return likeRight(fieldName, value, true);
    }

    public static SimpleExpression likeRight(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.LIKE_RIGHT);
    }

    public static SimpleExpression gt(String fieldName, String value) {
        return gt(fieldName, value, true);
    }

    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.GT);
    }

    public static SimpleExpression lt(String fieldName, String value) {
        return lt(fieldName, value, true);
    }

    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.LT);
    }

    public static SimpleExpression lte(String fieldName, String value) {
        return lte(fieldName, value, true);
    }

    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.LTE);
    }

    public static SimpleExpression gte(String fieldName, String value) {
        return gte(fieldName, value, true);
    }

    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Operator.GTE);
    }

    public static SimpleExpression isNull(String fieldName) {
        return new SimpleExpression(fieldName, null, Operator.NULL);
    }

    public static SimpleExpression isNotNull(String fieldName) {
        return new SimpleExpression(fieldName, null, Operator.NOT_NULL);
    }

    public static LogicalExpression and(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.AND);
    }

    public static LogicalExpression or(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.OR);
    }

    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value) {
        return in(fieldName, value, true);
    }

    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, Operator.OR);
    }
}