package com.zeketian.plugin.pojogenerator.entity;

import com.zeketian.plugin.pojogenerator.enums.FieldDataTypeEnum;

/**
 * @author zeke
 * @description 表字段实体类
 * @date created in 2022/10/3 23:04
 */
public class TableFieldEntity extends DdlEntity {

    /**
     * 标记是否为主键
     */
    private boolean primaryKey;


    /**
     * 数据类型
     */
    private FieldDataTypeEnum dataType;

    /**
     * 默认值
     */
    private String defaultValue;

}
