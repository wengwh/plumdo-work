package com.plumdo.common.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 逻辑条件表达式 用于复杂条件时使用，如但属性多对应值的OR查询等
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class LogicalExpression implements Criterion {
    private Criterion[] criterion;
    private Operator operator;

    public LogicalExpression(Criterion[] criterion, Operator operator) {
        this.criterion = criterion;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Criterion aCriterion : this.criterion) {
            predicates.add(aCriterion.toPredicate(root, query, builder));
        }
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[0]));
            default:
                return null;
        }
    }

}