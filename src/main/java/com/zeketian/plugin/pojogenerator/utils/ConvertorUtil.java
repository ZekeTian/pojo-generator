package com.zeketian.plugin.pojogenerator.utils;

import com.zeketian.plugin.pojogenerator.entity.TableEntity;
import com.zeketian.plugin.pojogenerator.entity.TableFieldEntity;
import com.zeketian.plugin.pojogenerator.vo.TableFieldVo;
import com.zeketian.plugin.pojogenerator.vo.TableVo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author zeke
 * @description 数据转换工具类
 * @date created in 2022/10/4 21:06
 */
@Mapper
public interface ConvertorUtil {

    ConvertorUtil INSTANCE = Mappers.getMapper(ConvertorUtil.class);

    /**
     * 将 TableEntity 转换成 TableVo
     *
     * @param entity 待转换的 TableEntity
     * @return 转换后的 TableVo
     */
    @Mapping(target = "name", source = "entity.name", qualifiedByName = "tableNameToJavaName")
    @Mapping(target = "sqlName", source = "entity.name", qualifiedByName = "removeUnusedCharacter")
    @Mapping(target = "comment", source = "entity.comment", qualifiedByName = "removeUnusedCharacter")
    TableVo tableEntityToTableVo(TableEntity entity);

    /**
     * 将 TableFieldEntity 转换成 TableFieldVo
     *
     * @param entity 待转换的 TableFieldEntity
     * @return 转换后的 TableFieldVo
     */
    @Mapping(target = "name", source = "entity.name", qualifiedByName = "fieldNameToJavaName")
    @Mapping(target = "sqlName", source = "entity.name", qualifiedByName = "removeUnusedCharacter")
    @Mapping(target = "comment", source = "entity.comment", qualifiedByName = "removeUnusedCharacter")
    @Mapping(target = "dataType", expression = "java(entity.getDataType().getValue())")
    TableFieldVo tableFieldEntityToTableFieldVo(TableFieldEntity entity);

    /**
     * 将 TableFieldEntity 列表转换成 TableFieldVo 列表
     *
     * @param entity 待转换的 TableFieldEntity 列表
     * @return 转换后的 TableFieldVo 列表
     */
    List<TableFieldVo> tableFieldEntityToTableFieldVo(List<TableFieldEntity> entity);

    /**
     * 将表的名字转换成 java 中的名字
     *
     * @param tableName 表的名字
     * @return 对应的 java 名字
     */
    @Named("tableNameToJavaName")
    default String tableNameToJavaName(String tableName) {
        String removedString = removeUnusedCharacter(tableName);
        return underlineToCamelCase(removedString, true);
    }

    /**
     * 删除 sql 中前后的无用字符
     *
     * @param src 原始字符串
     * @return 删除无用字符后的字符串
     */
    @Named("removeUnusedCharacter")
    default String removeUnusedCharacter(String src) {
        if (StringUtils.isEmpty(src)) {
            return src;
        }
        int start = 0, end = src.length() - 1;
        while (start < src.length() && isUnusedChar(src.charAt(start))) {
            ++start;
        }

        while (end >= 0 && isUnusedChar(src.charAt(end))) {
            --end;
        }

        if (start > end) {
            return "";
        }

        return src.substring(start, end + 1);
    }

    /**
     * 将下划线命名转换成驼峰命名
     *
     * @param underline  下划线命名
     * @param capitalize true，首字母大写；false，首字母小写
     * @return 对应的驼峰命名
     */
    @Named("underlineToCamelCase")
    default String underlineToCamelCase(String underline, boolean capitalize) {
        if (StringUtils.isEmpty(underline)) {
            return underline;
        }

        StringBuilder builder = new StringBuilder();
        boolean upperCase = capitalize;

        // 删除前导的 '_'
        int start = 0;
        while (start < underline.length() && underline.charAt(start) == '_') {
            ++start;
        }

        // 将下划线转换成驼峰
        for (int i = start; i < underline.length(); ++i) {
            if (underline.charAt(i) == '_') {
                upperCase = true;
                continue;
            }
            if (upperCase) {
                builder.append(Character.toUpperCase(underline.charAt(i)));
                upperCase = false;
            } else {
                builder.append(underline.charAt(i));
            }
        }

        return builder.toString();
    }

    /**
     * 判断 ch 是否为无用字符
     *
     * @param ch 待判断的字符
     * @return true，字符 ch 为无用字符；false，字符 ch 不是无用字符
     */
    default boolean isUnusedChar(char ch) {
        return (ch == '_' || ch == '`' || ch == '\'' || ch == '\"');
    }

    /**
     * 将表的字段名字转换成 java 中的名字
     *
     * @param fieldName 表的字段名字
     * @return 对应的 java 名字
     */
    @Named("fieldNameToJavaName")
    default String fieldNameToJavaName(String fieldName) {
        String removedString = removeUnusedCharacter(fieldName);
        return underlineToCamelCase(removedString, false);
    }

}
