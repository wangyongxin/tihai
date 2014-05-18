<?xml version="1.0" encoding="GBK"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tihai.dao.${(entity)!}Dao" >
	
  <resultMap id="BaseResultMap" type="com.tihai.entity.${(entity)!}" >
	<result column="ID" property="id" />
	<result column="CREATEDATE" property="createDate" />
	<result column="MODIFYDATE" property="modifyDate" />
	<#list properties as property>
	<result column="${property?upper_case}" property="${property}" />
	</#list>
  </resultMap>
  
  <sql id="selectBase" >
  	select ID,CREATEDATE,MODIFYDATE,<#list properties as property>${property?upper_case}<#if property_index != properties?size-1>,</#if></#list>
    from ${(entity?upper_case)!} ${(entity?upper_case?substring(0,1))!}
  </sql>
  <select id="find" resultMap="BaseResultMap" parameterType="Long" >
    <include refid="selectBase" />
    where ${(entity?upper_case?substring(0,1))!}.ID = <#noparse>#{ id}</#noparse>
  </select>
  
  <select id="findAll" resultMap="BaseResultMap" >
    <include refid="selectBase" />
  </select>
  
  <select id="findList" resultMap="BaseResultMap" parameterType="java.util.Map">
    <include refid="selectBase" />
     limit <#noparse>#{ first} , #{ count}</#noparse>
  </select>
  
  <select id="findPage" resultMap="BaseResultMap" parameterType="com.tihai.common.Pageable" statementType="STATEMENT">
    <include refid="selectBase" />
    <where>
    <if test="searchValue != null and searchValue != ''">
    <#noparse>${searchProperty} like '%${searchValue}%'</#noparse>
    </if>
    </where>
    <if test="orderProperty != null and orderProperty != ''">
     order by <#noparse>${ orderProperty}</#noparse> 
	     <if test="orderDirection != null and orderDirection != ''">
	      <#noparse>${ orderDirection}</#noparse> 
	    </if>
    </if>
     limit <#noparse>${ first} , ${ pageSize}</#noparse>
  </select>
  
  <select id="countAll" resultType="Long">
    select count(ID)
    from ${(entity?upper_case)!} ${(entity?upper_case?substring(0,1))!}
  </select>
  
  <select id="count" resultType="Long" parameterType="String">
    select count(ID)
    from ${(entity?upper_case)!} ${(entity?upper_case?substring(0,1))!} 
    <if test="conditions != null and conditions != ''">
    where <#noparse>#{ conditions}</#noparse>
    </if>
  </select>
  
  <insert id="save" parameterType="com.tihai.entity.${(entity)!}">
  		insert into ${(entity?upper_case)!} (CREATEDATE,MODIFYDATE,<#list properties as property>${property?upper_case}<#if property_index != properties?size-1>,</#if></#list>)
  		values (<#noparse>#{ createDate}</#noparse>,
  				<#noparse>#{ modifyDate}</#noparse>,
		  		<#list properties as property>
		  		<#noparse>#{ </#noparse>${property}<#noparse>}</#noparse><#if property_index != properties?size-1>,
		  		</#if>
		  		</#list>
		  		)
  		 <selectKey resultType="Long" keyProperty="id" >
		  SELECT LAST_INSERT_ID() as ID
		 </selectKey>
  </insert>
  
  <update id="update" parameterType="com.tihai.entity.${(entity)!}" >
	    update ${(entity?upper_case)!}
	    set
			MODIFYDATE = <#noparse>#{ modifyDate}</#noparse>,
			<#list properties as property>
	  		${property?upper_case} = <#noparse>#{ </#noparse>${property}<#noparse>}</#noparse><#if property_index != properties?size-1>,
	  		</#if>
	  		</#list>
	    where  ID = <#noparse>#{ id}</#noparse>
  </update>
  
  <select id="deleteById" parameterType="Long" >
    delete from ${(entity?upper_case)!} ${(entity?upper_case?substring(0,1))!}
    where ${(entity?upper_case?substring(0,1))!}.ID = <#noparse>#{ id}</#noparse>
  </select>
  
  <select id="delete" parameterType="com.tihai.entity.${(entity)!}" >
    delete from ${(entity?upper_case)!} ${(entity?upper_case?substring(0,1))!}
    where ${(entity?upper_case?substring(0,1))!}.ID = <#noparse>#{ id}</#noparse>
  </select>
  
  <select id="findForUpdate" parameterType="Long" >
    <include refid="selectBase" />
    where ${(entity?upper_case?substring(0,1))!}.ID = <#noparse>#{ id}</#noparse>
    for update
  </select>
  
  <select id="getIdentifier" resultType="Long" parameterType="com.tihai.entity.${(entity)!}" >
  	SELECT <#noparse>#{ id}</#noparse> FROM DUAL 
  </select>
  
  
  
  
</mapper>