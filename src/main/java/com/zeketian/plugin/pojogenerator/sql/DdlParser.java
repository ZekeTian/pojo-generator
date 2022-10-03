package com.zeketian.plugin.pojogenerator.sql;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.zeketian.plugin.pojogenerator.entity.TableEntity;
import com.zeketian.plugin.pojogenerator.entity.TableFieldEntity;
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
    private String sql = "";
    private TableEntity tableEntity;
    private List<TableFieldEntity> fieldEntityList;

    public DdlParser(String sql) {
        this.tableEntity = new TableEntity();
        this.fieldEntityList = new ArrayList<>();
        this.sql = sql;
        CharStream charStream = CharStreams.fromString(sql);
        mySqlLexer = new MySqlLexer(charStream);
        mySqlParser = new MySqlParser(new CommonTokenStream(mySqlLexer));
    }

    public void parse() {
        MySqlParser.DdlStatementContext ddlStatementContext = mySqlParser.ddlStatement();
        walk(ddlStatementContext);
    }

    private void walk(ParseTree node) {
        if (node == null) {
            return;
        }

        if (node instanceof MySqlParser.TableNameContext) {
            tableEntity.setName(node.getText());
        } else if (node instanceof MySqlParser.TableOptionCommentContext) {
            tableEntity.setComment(node.getText());
        } else if (node instanceof MySqlParser.ColumnDeclarationContext) {
            TableFieldEntity fieldEntity = parseColumn(node, new TableFieldEntity());
            fieldEntityList.add(fieldEntity);
            return;
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            walk(node.getChild(i));
        }
    }

    private TableFieldEntity parseColumn(ParseTree node, TableFieldEntity fieldEntity) {
        if (node == null) {
            return fieldEntity;
        }
        if (node instanceof MySqlParser.UidContext) {
            fieldEntity.setName(node.getText());
        } else if (node instanceof MySqlParser.ColumnDefinitionContext) {
            // TODO 解析数据类型
        } else if (node instanceof MySqlParser.CommentColumnConstraintContext) {
            TerminalNode terminalNode = ((MySqlParser.CommentColumnConstraintContext)node).STRING_LITERAL();
            fieldEntity.setComment((terminalNode == null ? "" : terminalNode.getText()));
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            parseColumn(node.getChild(i), fieldEntity);
        }

        return fieldEntity;
    }

    public String getSql() {
        return sql;
    }

    public TableEntity getTable() {
        return tableEntity;
    }

    public List<TableFieldEntity> getTableFields() {
        return fieldEntityList;
    }
}
