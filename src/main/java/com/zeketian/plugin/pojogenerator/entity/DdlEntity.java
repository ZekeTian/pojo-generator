package com.zeketian.plugin.pojogenerator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zeke
 * @description ddl 实体类
 * @date created in 2022/10/3 22:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DdlEntity {

    /**
     * 名字
     */
    protected String name;

    /**
     * 注释
     */
    protected String comment;

}
