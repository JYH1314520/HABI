package com.habi.boot.system.auth.entity;

/**Auto Generated By  Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.habi.boot.system.base.annotation.TableGeneratedValue;
import com.habi.boot.system.config.DatabaseTypeConfig;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import com.habi.boot.system.base.BaseEntity;
import org.hibernate.validator.constraints.NotEmpty;
@Table(name = "sys_resource")
public class Resource  extends BaseEntity {

     public static final String FIELD_RESOURCE_ID = "resourceId";
     public static final String FIELD_URL = "url";
     public static final String FIELD_TYPE = "type";
     public static final String FIELD_NAME = "name";
     public static final String FIELD_DESCRIPTION = "description";
     public static final String FIELD_LOGIN_REQUIRE = "loginRequire";
     public static final String FIELD_ACCESS_CHECK = "accessCheck";


     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
      @TableGeneratedValue(databaseType = DatabaseTypeConfig.databaseType)
     @Column(name = "RESOURCE_ID")
     private Long resourceId;

     @NotEmpty
     @Length(max = 255)
     @Column(name = "URL")
     private String url; //URL

     @Length(max = 15)
     @Column(name = "TYPE")
     private String type; //资源类型

     @Length(max = 40)
     @Column(name = "NAME")
     private String name; //名称

     @Length(max = 240)
     @Column(name = "DESCRIPTION")
     private String description; //说明

     @Length(max = 1)
     @Column(name = "LOGIN_REQUIRE")
     private String loginRequire; //是否必须登录

     @Length(max = 1)
     @Column(name = "ACCESS_CHECK")
     private String accessCheck; //access校验


     public void setResourceId(Long resourceId){
         this.resourceId = resourceId;
     }

     public Long getResourceId(){
         return resourceId;
     }

     public void setUrl(String url){
         this.url = url;
     }

     public String getUrl(){
         return url;
     }

     public void setType(String type){
         this.type = type;
     }

     public String getType(){
         return type;
     }

     public void setName(String name){
         this.name = name;
     }

     public String getName(){
         return name;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setLoginRequire(String loginRequire){
         this.loginRequire = loginRequire;
     }

     public String getLoginRequire(){
         return loginRequire;
     }

     public void setAccessCheck(String accessCheck){
         this.accessCheck = accessCheck;
     }

     public String getAccessCheck(){
         return accessCheck;
     }

     }