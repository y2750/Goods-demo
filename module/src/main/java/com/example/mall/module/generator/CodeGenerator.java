package com.example.mall.module.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CodeGenerator {
    private static final String TEMPLATE_ROOT = "/generator/templates/";

    public static void main(String[] args) throws IOException {
        Path projectRoot = findProjectRoot();
        Properties settings;
        if (args.length == 0) {
            try (InputStream input = CodeGenerator.class.getResourceAsStream(
                    "/generator/generator.properties")) {
                if (input == null) {
                    throw new IllegalStateException("找不到 generator/generator.properties");
                }
                settings = load(input);
            }
        } else {
            settings = load(projectRoot.resolve(args[0]));
        }

        Properties datasource = load(projectRoot.resolve(required(settings, "datasource.properties")));
        String url = required(datasource, "spring.datasource.url");
        String username = required(datasource, "spring.datasource.username");
        String password = datasource.getProperty("spring.datasource.password", "");
        String[] tables = split(required(settings, "tables"));
        if (tables.length == 0) {
            throw new IllegalArgumentException("tables 至少需要填写一张表，避免生成整个数据库");
        }
        Path outputRoot = projectRoot.resolve(required(settings, "output.root")).normalize();

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author(required(settings, "author"))
                        .outputDir(outputRoot.resolve("src/main/java").toString())
                        .disableOpenDir())
                .packageConfig(builder -> builder
                        .parent(required(settings, "parent.package"))
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .pathInfo(Collections.singletonMap(OutputFile.xml,
                                outputRoot.resolve("src/main/resources/mapper").toString())))
                .strategyConfig(builder -> {
                    builder.addInclude(tables);
                    builder.addTablePrefix(split(settings.getProperty("table.prefix", "")));
                    builder.entityBuilder().javaTemplate(TEMPLATE_ROOT + "entity.java");
                    builder.mapperBuilder()
                            .mapperTemplate(TEMPLATE_ROOT + "mapper.java")
                            .mapperXmlTemplate(TEMPLATE_ROOT + "mapper.xml");
                    // Service 模板生成普通 @Service 类，不继承 MP 的 IService/ServiceImpl。
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .serviceTemplate(TEMPLATE_ROOT + "service.java")
                            .disableServiceImpl();
                    builder.controllerBuilder().disable();
                })
                .injectionConfig(builder -> builder.beforeOutputFile(
                        (table, model) -> prepareModel(table, model, settings)))
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

        System.out.println("代码生成结束，输出目录：" + outputRoot);
        System.out.println("已有文件默认跳过；请查看上面的逐文件生成日志。");
    }

    /** 把主键和约定字段的信息集中提供给模板，避免模板里写死 id 类型。 */
    private static void prepareModel(TableInfo table, Map<String, Object> model, Properties settings) {
        List<TableField> keys = table.getFields().stream()
                .filter(TableField::isKeyFlag).collect(Collectors.toList());
        if (keys.size() != 1 || !keys.get(0).isKeyIdentityFlag()) {
            throw new IllegalArgumentException(table.getName() + " 必须有且只有一个自增主键");
        }
        TableField key = keys.get(0);
        requireType(key, "Integer", "Long");
        TableField created = field(table, required(settings, "column.create-time"));
        TableField updated = field(table, required(settings, "column.update-time"));
        TableField deleted = field(table, required(settings, "column.deleted"));
        requireType(created, "Integer", "Long");
        requireType(updated, "Integer", "Long");
        requireType(deleted, "Integer", "Long", "Byte", "Short", "Boolean");

        Set<String> imports = new TreeSet<>();
        for (TableField field : table.getFields()) {
            String fieldPackage = field.getColumnType().getPkg();
            if (fieldPackage != null && !fieldPackage.startsWith("java.lang.")) {
                imports.add(fieldPackage);
            }
        }
        model.put("entityImports", imports);
        model.put("pk", key);
        model.put("created", created);
        model.put("updated", updated);
        model.put("deleted", deleted);
        model.put("createdValue", "Long".equals(created.getPropertyType()) ? "timestamp" : "(int) timestamp");
        model.put("updatedValue", "Long".equals(updated.getPropertyType()) ? "timestamp" : "(int) timestamp");
        model.put("deletedZero", zero(deleted.getPropertyType()));
        model.put("selectColumns", table.getFields().stream()
                .map(f -> quote(f.getName()) + " AS " + quote(f.getPropertyName()))
                .collect(Collectors.joining(", ")));
        model.put("tableSql", quote(table.getName()));
    }

    private static TableField field(TableInfo table, String column) {
        return table.getFields().stream().filter(f -> f.getName().equals(column)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        table.getName() + " 缺少规范字段 " + column + "，请检查表结构或 generator.properties"));
    }

    private static void requireType(TableField field, String... types) {
        if (!Arrays.asList(types).contains(field.getPropertyType())) {
            throw new IllegalArgumentException(field.getName() + " 的类型 " + field.getPropertyType()
                    + " 不符合当前模板规范，支持：" + Arrays.toString(types));
        }
    }

    private static String zero(String type) {
        if ("Long".equals(type)) return "0L";
        if ("Byte".equals(type)) return "(byte) 0";
        if ("Short".equals(type)) return "(short) 0";
        if ("Boolean".equals(type)) return "false";
        return "0";
    }

    private static String quote(String name) {
        return "`" + name.replace("`", "``") + "`";
    }

    private static String[] split(String value) {
        return Arrays.stream(value.split(",")).map(String::trim)
                .filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    private static String required(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("缺少配置：" + name);
        }
        return value.trim();
    }

    private static Properties load(Path file) throws IOException {
        try (InputStream input = Files.newInputStream(file)) {
            return load(input);
        }
    }

    private static Properties load(InputStream input) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        return properties;
    }

    private static Path findProjectRoot() {
        Path path = Paths.get(System.getProperty("generator.project-dir",
                System.getProperty("user.dir"))).toAbsolutePath().normalize();
        while (path != null) {
            if (Files.isRegularFile(path.resolve("module/pom.xml"))) {
                return path;
            }
            path = path.getParent();
        }
        throw new IllegalStateException("找不到项目根目录，请设置 -Dgenerator.project-dir=项目绝对路径");
    }
}
