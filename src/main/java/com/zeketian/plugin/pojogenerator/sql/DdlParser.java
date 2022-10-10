package com.zeketian.plugin.pojogenerator.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

import com.zeketian.plugin.pojogenerator.entity.TableEntity;
import com.zeketian.plugin.pojogenerator.entity.TableFieldEntity;
import com.zeketian.plugin.pojogenerator.enums.FieldDataTypeEnum;
import com.zeketian.plugin.pojogenerator.sql.analyzer.MySqlLexer;
import com.zeketian.plugin.pojogenerator.sql.analyzer.MySqlParser;

/**
 * @author zeke
 * @description ddl 语句解析器
 * @date created in 2022/10/3 22:49
 */
public class DdlParser {

    private final MySqlParser mySqlParser;
    private final MySqlLexer mySqlLexer;
    /**
     * key：字段名，value：字段
     */
    private final Map<String, TableFieldEntity> fieldEntityMap;
    private String sql;
    private TableEntity tableEntity;

    public DdlParser(String sql) {
        this.tableEntity = new TableEntity();
        this.fieldEntityMap = new HashMap<>();
        this.sql = sql;
        CharStream charStream = CharStreams.fromString(sql);
        mySqlLexer = new MySqlLexer(charStream);
        mySqlParser = new MySqlParser(new CommonTokenStream(mySqlLexer));
    }

    public void parse() throws IllegalArgumentException {
        try {
            MySqlParser.DdlStatementContext ddlStatementContext = mySqlParser.ddlStatement();
            walk(ddlStatementContext);
        } catch (Exception e) {
            throw new IllegalArgumentException(String
                .format("Failed to parse sql, please check whether the sql syntax is correct!\n%s", e.getMessage()));
        }
    }

    private void walk(ParseTree node) {
        if (node == null) {
            return;
        }

        if (node instanceof MySqlParser.TableNameContext) {
            tableEntity.setName(node.getText());
            return;
        }
        if (node instanceof MySqlParser.TableOptionCommentContext) {
            tableEntity.setComment(((MySqlParser.TableOptionCommentContext) node).STRING_LITERAL().getText());
            return;
        }
        if (node instanceof MySqlParser.ColumnDeclarationContext) {
            TableFieldEntity parsedFieldEntity = parseColumn(node, new TableFieldEntity());
            if (StringUtils.isEmpty(parsedFieldEntity.getName())) {
                throw new RuntimeException("failed to parse column");
            }

            // 判断该字段是否已经被解析到（如在解析 primaryKey 时，可能解析到该字段）
            TableFieldEntity oldFiledEntity = fieldEntityMap.get(parsedFieldEntity.getName());
            if (oldFiledEntity != null) {
                parsedFieldEntity.setPrimaryKey(oldFiledEntity.isPrimaryKey());
            }
            fieldEntityMap.put(parsedFieldEntity.getName(), parsedFieldEntity);
            return;
        }

        if (node instanceof MySqlParser.PrimaryKeyTableConstraintContext) {
            String primaryKey = parsePrimaryKey((MySqlParser.PrimaryKeyTableConstraintContext) node);
            if (!StringUtils.isEmpty(primaryKey)) {
                TableFieldEntity fieldEntity = fieldEntityMap.getOrDefault(primaryKey, new TableFieldEntity());
                fieldEntity.setName(primaryKey);
                fieldEntity.setPrimaryKey(true);
                fieldEntityMap.put(primaryKey, fieldEntity);
            }

            return;
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            walk(node.getChild(i));
        }
    }

    /**
     * 解析出主键的字段名称
     *
     * @param node 语法树节点
     * @return 主键字段名称
     */
    private String parsePrimaryKey(MySqlParser.PrimaryKeyTableConstraintContext node) {
        if (node == null || node.indexColumnNames() == null) {
            return "";
        }
        List<MySqlParser.IndexColumnNameContext> indexColumnNames = node.indexColumnNames().indexColumnName();
        if (indexColumnNames == null || indexColumnNames.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(",");
        for (MySqlParser.IndexColumnNameContext nameContext : indexColumnNames) {
            joiner.add(nameContext.getText());
        }

        return joiner.toString();
    }

    private TableFieldEntity parseColumn(ParseTree node, TableFieldEntity fieldEntity) {
        if (node == null) {
            return fieldEntity;
        }

        // 解析字段名
        if (node instanceof MySqlParser.UidContext) {
            fieldEntity.setName(node.getText());
            return fieldEntity;
        }

        // 解析字段数据类型
        FieldDataTypeEnum dataTypeEnum = parseDataType(node, fieldEntity);
        if (dataTypeEnum != null) {
            // 从当前节点处成功解析出数据类型后，直接返回即可，不需要再处理它的子节点
            fieldEntity.setDataType(dataTypeEnum);
            return fieldEntity;
        }

        // 解析字段的注释
        if (node instanceof MySqlParser.CommentColumnConstraintContext) {
            TerminalNode terminalNode = ((MySqlParser.CommentColumnConstraintContext) node).STRING_LITERAL();
            fieldEntity.setComment((terminalNode == null ? "" : terminalNode.getText()));
            return fieldEntity;
        }

        // 从当前节点无法出解析出字段的信息，则继续解析它的子节点
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            parseColumn(node.getChild(i), fieldEntity);
        }

        return fieldEntity;
    }

    private FieldDataTypeEnum parseDataType(ParseTree node, TableFieldEntity fieldEntity) {
        if (node instanceof MySqlParser.DimensionDataTypeContext) {
            // 用于衡量大小的数据类型
            MySqlParser.DimensionDataTypeContext dimensionNode = ((MySqlParser.DimensionDataTypeContext) node);
            FieldDataTypeEnum typeEnum = parseDimensionDataType(dimensionNode);
            if (typeEnum == null) {
                throw new RuntimeException(
                        String.format("failed to parse datatype, sql text: %s", dimensionNode.getText()));
            }
            return typeEnum;
        }

        if (node instanceof MySqlParser.StringDataTypeContext) {
            // 字符串类型
            return FieldDataTypeEnum.STRING_TYPE;
        }

        if (node instanceof MySqlParser.SimpleDataTypeContext) {
            MySqlParser.SimpleDataTypeContext simpleDataNode = (MySqlParser.SimpleDataTypeContext) node;
            if (simpleDataNode.DATE() == null) {
                throw new RuntimeException("failed to parse SimpleDataTypeContext, sql text: " + node.getText());
            }
            // 日期类型
            return FieldDataTypeEnum.DATE_TYPE;

        }
        return null;
    }

    private FieldDataTypeEnum parseDimensionDataType(MySqlParser.DimensionDataTypeContext dimensionNode) {
        if (dimensionNode.TIMESTAMP() != null || dimensionNode.DATETIME() != null) {
            return FieldDataTypeEnum.TIMESTAMP_TYPE;
        }

        if (dimensionNode.INTEGER() != null || dimensionNode.TINYINT() != null || dimensionNode.SMALLINT() != null
            || dimensionNode.MEDIUMINT() != null || dimensionNode.INT() != null) {
            return FieldDataTypeEnum.INTEGER_TYPE;
        }

        if (dimensionNode.BIGINT() != null) {
            return FieldDataTypeEnum.LONG_TYPE;
        }

        if (dimensionNode.DECIMAL() != null) {
            return FieldDataTypeEnum.BIG_DECIMAL_TYPE;
        }

        if (dimensionNode.FLOAT() != null || dimensionNode.FLOAT4() != null || dimensionNode.FLOAT8() != null) {
            return FieldDataTypeEnum.FLOAT_TYPE;
        }

        if (dimensionNode.DOUBLE() != null) {
            return FieldDataTypeEnum.DOUBLE_TYPE;
        }

        return null;
    }

    public String getSql() {
        return sql;
    }

    public TableEntity getTable() {
        return tableEntity;
    }

    public List<TableFieldEntity> getTableFields() {
        return new ArrayList<>(fieldEntityMap.values());
    }
}
