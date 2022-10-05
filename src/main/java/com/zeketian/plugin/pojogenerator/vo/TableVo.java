package com.zeketian.plugin.pojogenerator.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zeke
 * @description 表
 * @date created in 2022/10/4 20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableVo {

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

    private String engine;

    private String charset;

}
