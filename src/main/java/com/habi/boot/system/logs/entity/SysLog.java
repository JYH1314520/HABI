package com.habi.boot.system.logs.entity;


import com.habi.boot.system.base.BaseEntity;
import com.habi.boot.system.base.annotation.TableGeneratedValue;
import com.habi.boot.system.config.DatabaseTypeConfig;

import javax.persistence.*;

/**
 * <p>
 * 系统日志
 * </p>
 *
 */
@Table(name = "sys_log")
@Entity
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

	@Id
	@TableGeneratedValue(databaseType = DatabaseTypeConfig.databaseType)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long logId;

    /**
     * 请求类型
     */
	private String type;
    /**
     * 日志标题
     */
	private String title;
    /**
     * 操作IP地址
     */
	private String remoteAddr;
    /**
     * 操作用户昵称
     */
	private String username;
    /**
     * 请求URI
     */
	private String requestUri;
    /**
     * 操作方式
     */
	private String httpMethod;
    /**
     * 请求类型.方法
     */
	private String classMethod;

	/**
	 * 操作提交头的数据
	 */
	private String requestHeader;


	/**
	 * 操作提交body的数据
	 */
	private String requestBody;
    /**
     * 操作提交的数据
     */
	private String params;
    /**
     * sessionId
     */
	private String sessionId;
    /**
     * 返回内容
     */
	private String response;
    /**
     * 方法执行时间
     */
	private Long useTime;
    /**
     * 浏览器信息
     */
	private String browser;
    /**
     * 地区
     */
	private String area;
    /**
     * 省
     */
	private String province;
    /**
     * 市
     */
	private String city;
    /**
     * 网络服务提供商
     */
	private String isp;
    /**
     * 异常信息
     */
	private String exception;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getClassMethod() {
		return classMethod;
	}

	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}

	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Long getUseTime() {
		return useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}


	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	@Override
	public String toString() {
		return "SysLog{" +
			", type=" + type +
			", title=" + title +
			", remoteAddr=" + remoteAddr +
			", username=" + username +
			", requestUri=" + requestUri +
			", httpMethod=" + httpMethod +
			", classMethod=" + classMethod +
			", params=" + params +
			", sessionId=" + sessionId +
			", response=" + response +
			", useTime=" + useTime +
			", browser=" + browser +
			", area=" + area +
			", province=" + province +
			", city=" + city +
			", isp=" + isp +
			", exception=" + exception +
			"}";
	}
}
