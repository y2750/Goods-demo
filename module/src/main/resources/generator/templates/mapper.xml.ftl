<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <!-- 自增主键不参与 INSERT；通过 entity.${pk.propertyName} 回填。只跳过 null，允许空字符串。 -->
    <insert id="insert" useGeneratedKeys="true" keyProperty="entity.${pk.propertyName}" keyColumn="${pk.name}">
        INSERT INTO ${tableSql}
        <trim prefix="(" suffix=")" suffixOverrides=",">
<#list table.fields as field>
<#if !field.keyFlag && field.name != deleted.name>
            <if test="entity.${field.propertyName} != null">`${field.name}`,</if>
</#if>
</#list>
            `${deleted.name}`
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
<#list table.fields as field>
<#if !field.keyFlag && field.name != deleted.name>
            <if test="entity.${field.propertyName} != null"><#noparse>#{entity.</#noparse>${field.propertyName}},</if>
</#if>
</#list>
            0
        </trim>
    </insert>

    <!-- 创建时间和删除状态不通过普通修改接口更新。 -->
    <update id="update">
        UPDATE ${tableSql}
        <set>
<#list table.fields as field>
<#if !field.keyFlag && field.name != created.name && field.name != deleted.name && field.name != updated.name>
            <if test="entity.${field.propertyName} != null">
                `${field.name}` = <#noparse>#{entity.</#noparse>${field.propertyName}},
            </if>
</#if>
</#list>
            `${updated.name}` = <#noparse>#{entity.</#noparse>${updated.propertyName}}
        </set>
        WHERE `${pk.name}` = <#noparse>#{entity.</#noparse>${pk.propertyName}}
          AND `${deleted.name}` = 0
    </update>
</mapper>
