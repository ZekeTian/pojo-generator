package com.zeketian.plugin.pojogenerator.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zeke
 * @description 表字段
 * @date created in 2022/10/4 20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableFieldVo {
    /**
     * 名字
     */
    private String name;

    /**
     * sql 中的名字
     */
    private String sqlName;

    /**
     * 注释
     */
    private String comment;

    /**
     * 标记是否为主键
     */
    private boolean primaryKey;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 默认值
     */
    private String defaultValue;
}
