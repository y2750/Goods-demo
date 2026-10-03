package com.example.mall.module.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.EnumMap;
import java.util.Map;

/**
 * ClassName: CodeGenerator
 * Package: com.example.mall.module.generator
 * Description:
 *
 * @Author
 * @Create 2026/10/3 11:54
 * @Version 1.0
 */
public class CodeGenerator {
    public static void main(String[] args) {
        String projectPath = "D:/java_code/demo1";
        Map<OutputFile, String> outputPaths = new EnumMap<>(OutputFile.class);
        outputPaths.put(OutputFile.xml,
                projectPath + "/module/src/main/resources/mapper");
        outputPaths.put(OutputFile.controller,
                projectPath + "/main/src/main/java/com/example/mall/main/controller");

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/sm_db", "root", "a600523")
                .globalConfig(builder -> {
                    builder.author("ny") // 设置作者
                            .outputDir(projectPath + "/module/src/main/java")
                            .disableOpenDir();
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.example.mall") // 设置父包名
                                .entity("module.entity")
                                .mapper("module.mapper")
                                .service("module.service")
                                .serviceImpl("module.service.impl")
                                .controller("main.controller")
                                .pathInfo(outputPaths)
                )
                .strategyConfig(builder -> {
                    builder.addInclude("user");
                    builder.entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("is_deleted");
                    builder.mapperBuilder()
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                    builder.controllerBuilder()
                            .enableRestStyle()
                            .formatFileName("%sController");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
