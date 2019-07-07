package com.plumdo.common.jpa;

import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;

/**
 * 简单条件表达式
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class SimpleExpression implements Criterion {
    private String fieldName;
    private Object value;
    private Operator operator;

    protected SimpleExpression(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path expression = null;
        if (fieldName.contains(".")) {
            String[] names = StringUtils.split(fieldName, ".");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        } else {
            expression = root.get(fieldName);
        }
        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case NULL:
                return builder.isNull(expression);
            case NOT_NULL:
                return builder.isNotNull(expression);
            case LIKE:
                return builder.like((Expression<String>) expression, "%" + value + "%");
            case LIKE_LEFT:
                return builder.like((Expression<String>) expression, "%" + value);
            case LIKE_RIGHT:
                return builder.like((Expression<String>) expression, value + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            default:
                return null;
        }
    }

}