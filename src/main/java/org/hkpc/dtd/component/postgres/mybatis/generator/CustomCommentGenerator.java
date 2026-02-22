package org.hkpc.dtd.component.postgres.mybatis.generator;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * We added the Swagger annotation here, so that we can just copy the Mybatis model class to the controller DTO class, and the Swagger annotation will be copied as well.
 * Then you can see the Field Description in the Swagger UI.
 * So All field definitions originate fundamentally from the database table comments.
 */
public class CustomCommentGenerator implements CommentGenerator {
    private final Properties properties;

    public CustomCommentGenerator() {
        properties = new Properties();
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // 获取自定义的 properties
        this.properties.putAll(properties);
    }

    /**
     * 重写给实体类加的注释 Override the comments added to the entity class
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//        String author = properties.getProperty("author");
        String tableName=introspectedTable.getFullyQualifiedTable().toString();
        String dateFormat = properties.getProperty("dateFormat", "yyyy-MM-dd");
        String description=null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        // 获取表注释 Get table comments
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine("import io.swagger.v3.oas.annotations.media.Schema;");
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * Table name: " + tableName);
        topLevelClass.addJavaDocLine(" * <p>");
        if(StringUtils.hasLength(remarks)){
            topLevelClass.addJavaDocLine(" * Description: " + remarks.replace("\r\n", "<p>\n * "));
        }
        topLevelClass.addJavaDocLine(" *");
//        topLevelClass.addJavaDocLine(" * @author " + author);
//        topLevelClass.addJavaDocLine(" * @date   " + dateFormatter.format(new Date()));
        topLevelClass.addJavaDocLine(" */");

        if(StringUtils.hasLength(remarks)){
            topLevelClass.addJavaDocLine("@Schema(description = \"\"\"\r\n        Table name: "+tableName+"<br/>Description: "+remarks.replace("\r\n", "<br/>")+"\r\n        \"\"\")");
        }else {
            topLevelClass.addJavaDocLine("@Schema(description = \"Table name: "+tableName+"\")");
        }



    }

    /**
     * 重写给实体类字段加的注释 Override the comments added to the entity class field
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = StringUtils.hasLength(introspectedColumn.getRemarks())?introspectedColumn.getRemarks():"";
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
        if(StringUtils.hasLength(remarks)){
            field.addJavaDocLine("@Schema(description = \"\"\"\r\n            "+ remarks+"\r\n            \"\"\")");

        }
    }

    /**
     * 重写给实体类get方法加的注释
     */
//    @Override
//    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        // 获取表注释
//        String remarks = introspectedColumn.getRemarks();
//        method.addJavaDocLine("/**");
//        method.addJavaDocLine(" * " + method.getName());
//        method.addJavaDocLine(" */");
//    }
}
