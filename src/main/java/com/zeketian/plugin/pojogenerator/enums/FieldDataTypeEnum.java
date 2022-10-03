package com.zeketian.plugin.pojogenerator.enums;

/**
 * @author zeke
 * @description 字段数据类型
 * @date created in 2022/10/3 23:10
 */
public enum FieldDataTypeEnum {
    /**
     * 字符串类型，对应 sql 中的 char、varchar、text、longtext 等字符类型
     */
    STRING_TYPE(100),

    /**
     * 整数类型，对应 int、small int
     */
    INTEGER_TYPE(200),

    /**
     * 长整数类型，对应 sql 中的 bigint
     */
    LONG_TYPE(201),

    /**
     * 浮点型，对应 sql 中的 float
     */
    FLOAT_TYPE(202),

    /**
     * 双精度浮点型，对应 sql 中的 double
     */
    DOUBLE_TYPE(203),

    /**
     * 时间戳类型，对应 sql 的  timestamp、datetime
     */
    TIMESTAMP_TYPE(301),

    /**
     * 日期类型，对应 sql 的 date
     */
    DATE_TYPE(302);


    private Integer code;

    private FieldDataTypeEnum(Integer code) {
        this.code = code;
    }

}
