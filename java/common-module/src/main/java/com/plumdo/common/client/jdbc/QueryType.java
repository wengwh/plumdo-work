package com.plumdo.common.client.jdbc;

/**
 * 查询的类型枚举类
 *
 * @author wengwh
 * @date 2019/7/7
 */
public enum QueryType {
    /**
     * 等于运算
     */
    EQ("=", "等于"),
    NOT_EQ("<>", "不等于"),
    GT(">", "大于"),
    LT("<", "小于"),
    GE(">=", "大于等于"),
    LE("<=", "小于等于"),
    EQ_COLUMN("=", "等于其他字段"),
    NOT_EQ_COLUMN("<>", "不等于其他字段"),
    GT_COLUMN(">", "大于其他字段"),
    LT_COLUMN("<", "小于其他字段"),
    GE_COLUMN("<=", "大于等于其他字段"),
    LE_COLUMN(">=", "小于等于其他字段"),
    LIKE("like", "模糊匹配,%value%"),
    LIKE_LEFT("like", "左模糊匹配,%value"),
    LIKE_RIGHT("like", "右模糊匹配,value%"),
    IN("in", "包含"),
    NOT_IN("not in", "不包含"),
    BETWEEN("between", "两者之间"),
    NULL("is null", "空判断"),
    NOT_NULL("is not null", "非空判断"),
    EQ_AND("=", "两个条件AND");

    /**
     * 主键
     */
    private final String key;

    /**
     * 描述
     */
    private final String desc;

    QueryType(final String key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
