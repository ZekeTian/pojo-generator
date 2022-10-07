package com.zeketian.plugin.pojogenerator.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

import com.zeketian.plugin.pojogenerator.entity.TableEntity;
import com.zeketian.plugin.pojogenerator.entity.TableFieldEntity;
import com.zeketian.plugin.pojogenerator.sql.DdlParser;
import com.zeketian.plugin.pojogenerator.utils.ConvertorUtil;
import com.zeketian.plugin.pojogenerator.utils.VelocityConstants;
import com.zeketian.plugin.pojogenerator.vo.TableFieldVo;
import com.zeketian.plugin.pojogenerator.vo.TableVo;

/**
 * @author zeke
 * @description 用于处理相关逻辑
 * @date created in 2022/10/6 20:31
 */
public class MainController {

    private final ConvertorUtil convertor = ConvertorUtil.INSTANCE;

    public void generatePojo(String projectPath, String sql, String packageName, Boolean enableMybatisPlus)
        throws IOException, IllegalArgumentException {
        // 解析 sql
        DdlParser ddlParser = new DdlParser(sql);
        try {
            ddlParser.parse();
        } catch (IllegalArgumentException e) {
            throw e;
        }
        TableEntity table = ddlParser.getTable();
        List<TableFieldEntity> tableFields = ddlParser.getTableFields();

        if (StringUtils.isEmpty(table.getName())) {
            throw new RuntimeException("Failed to parse sql");
        }

        // 确定存在的数据类型
        // 数据类型 map，key: 数据类型，value: 该类型是否存在（true，表示该类型存在）
        Map<String, Boolean> dataTypeMapConfig = new HashMap<>(tableFields.size());
        for (TableFieldEntity fieldEntity : tableFields) {
            String dataTypeKey = String.format("has%s", fieldEntity.getDataType().getValue());
            dataTypeMapConfig.put(dataTypeKey, true);
        }

        // 初始化引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
        ve.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
        // 下面这种是读取自身 jar 包里面的文件，但是因为使用的是 idea 插件 sdk 中的 velocity，所以此时 velocity 是读取 idea 插件 sdk 的 jar 包，而不是读取
        // pojo-generator 插件的 jar 包。
        // 所以这种 classpath 的方式不行
        // ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        // ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        // 因为存在 “idea 插件 sdk 的 jar”（velocity 所在的 jar 包） 和 "pojo-generator 的 jar"（模板文件所在的 jar 包），
        // 所以为了让 velocity 能够读取到模板文件，需要使用如下的 JarResourceLoader 方式。
        // ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "jar");
        // ve.setProperty("jar.resource.loader.class", JarResourceLoader.class.getName());
        // URL resource = getClass().getResource(VelocityConstants.TEMPLATE_FILE_PATH);
        // String jarPath = resource.toString();
        // // 指定 jar 包路径
        // ve.setProperty("jar.resource.loader.path", jarPath.substring(0, jarPath.indexOf('!')));

        // 关闭 velocity 的日志输出
        ve.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());

        ve.init();

        // 设置 VelocityContext
        TableVo tableVo = convertor.tableEntityToTableVo(table);
        List<TableFieldVo> tableFieldVos = convertor.tableFieldEntityToTableFieldVo(tableFields);
        VelocityContext vc = new VelocityContext(dataTypeMapConfig);
        setVariable(vc, packageName, tableVo, tableFieldVos, enableMybatisPlus);

        // 获取模板，生成内容，并将内容保存成文件
        File outputFile = createOutputFile(projectPath, packageName, tableVo.getName());
        try (InputStream inputStream = getClass().getResourceAsStream(VelocityConstants.TEMPLATE_FILE_PATH);
            BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            ve.evaluate(vc, writer, "generateJavaFile", inputStream);
        } catch (IOException e) {
            throw e;
        }
    }

    private void setVariable(VelocityContext vc, String packageName, TableVo table, List<TableFieldVo> tableFields,
        Boolean enableMybatisPlus) {
        vc.put(VelocityConstants.PACKAGE_NAME, packageName);
        vc.put(VelocityConstants.ENABLE_MYBATIS_PLUS, enableMybatisPlus);

        String user = System.getenv().get("USERNAME");
        vc.put(VelocityConstants.USER, user);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createTime = dateFormat.format(new Date(System.currentTimeMillis()));
        vc.put(VelocityConstants.CREATE_TIME, createTime);

        vc.put(VelocityConstants.TABLE, table);

        vc.put(VelocityConstants.COLUMN_LIST, tableFields);
    }

    private File createOutputFile(String projectPath, String packageName, String tableName) throws IOException {
        String outputPath = generateJavaFilePath(projectPath, packageName, tableName);
        File outputFile = new File(outputPath);
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        return outputFile;
    }

    private String generateJavaFilePath(String projectPath, String packageName, String tableName) {
        String suffix = "Pojo";
        String packagePath = packageName.replaceAll("\\.", "/");
        return String.format("%s/src/main/java/%s/%s%s.java", projectPath, packagePath, tableName, suffix);
    }

}
