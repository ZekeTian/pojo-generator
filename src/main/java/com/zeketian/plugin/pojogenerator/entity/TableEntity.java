package com.zeketian.plugin.pojogenerator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zeke
 * @description 表实体类
 * @date created in 2022/10/3 23:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableEntity {

    /**
     * 名字
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 数据库引擎
     */
    private String engine;

    /**
     * 数据库字符集
     */
    private String charset;
}
