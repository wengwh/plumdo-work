package com.plumdo.common.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 条件接口 用户提供条件表达式接口
 *
 * @author wengwenhui
 * @date 2018年3月29日
 */
public interface Criterion {
	public enum Operator {
		EQ, NE, LIKE, LIKE_LEFT, LIKE_RIGHT, GT, LT, GTE, LTE, AND, OR, NULL, NOT_NULL
	}

	Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}