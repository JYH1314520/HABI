package com.habi.boot.system.base;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.habi.boot.system.base.utils.TimeZoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BaseEntity implements Serializable, IBaseEntity {
    public static final String FIELD_REQUEST_ID = "requestId";
    public static final String FIELD_PROGRAM_ID = "programId";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_LAST_UPDATE_LOGIN = "lastUpdateLogin";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";
    private static final Pattern COL_PATTERN = Pattern.compile("[\\d\\w_]+");
    @Transient
    @XmlTransient
    @JsonIgnore
    protected Map<String, Object> innerMap = new HashMap();
    @JsonInclude(Include.NON_NULL)
    @Transient
    private String __id;
    @JsonInclude(Include.NON_NULL)
    @Transient
    @XmlTransient
    private String __status;
    @Transient
    private Map<String, Map<String, String>> __tls = Collections.emptyMap();
    @JsonInclude(Include.NON_NULL)
    @Transient
    private String sortname;
    @JsonInclude(Include.NON_NULL)
    @Transient
    private String sortorder;
    @JsonInclude(Include.NON_NULL)
    @Transient
    private String _token;
    @JsonIgnore
    @Column
    private Long requestId = -1L;
    @JsonIgnore
    @Column
    private Long programId = -1L;
    @Column(
            updatable = false
    )
    private Long objectVersionNumber;
    @JsonIgnore
    @Column(
            updatable = false
    )
    private Long createdBy;
    @JsonIgnore
    @Column(
            updatable = false
    )
    private Date creationDate;
    @JsonIgnore
    @Column
    private Long lastUpdatedBy;
    @JsonIgnore
    @Column
    private Date lastUpdateDate;
    @JsonIgnore
    @Column
    private Long lastUpdateLogin;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attributeCategory;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute1;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute2;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute3;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute4;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute5;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute6;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute7;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute8;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute9;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute10;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute11;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute12;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute13;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute14;
    @JsonInclude(Include.NON_NULL)
    @Column
    private String attribute15;
    private static final ToStringStyle TO_STRING_STYLE = new StandardToStringStyle() {
        public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
            if (value != null) {
                if (!fieldName.startsWith("_")) {
                    if (value instanceof Date) {
                        value = DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss", TimeZoneUtil.getTimeZone());
                    }

                    super.append(buffer, fieldName, value, fullDetail);
                }
            }
        }
    };

    public BaseEntity() {
    }

    @Transient
    @JsonIgnore
    protected <T> T innerGet(String key) {
        return (T) this.innerMap.get(key);
    }

    protected void innerSet(String key, Object value) {
        this.innerMap.put(key, value);
    }

    public String get__id() {
        return this.__id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public String get__status() {
        return this.__status;
    }

    public void set__status(String __status) {
        this.__status = __status;
    }

    @JsonIgnore
    @XmlTransient
    public Map<String, Map<String, String>> get__tls() {
        return this.__tls;
    }

    @JsonProperty
    public void set__tls(Map<String, Map<String, String>> __tls) {
        if (__tls != null) {
            this.__tls = __tls;
        }

    }

    public String getSortname() {
        return this.sortname;
    }

    public void setSortname(String sortname) {
        if (StringUtils.isNotEmpty(sortname) && !COL_PATTERN.matcher(sortname).matches()) {
            throw new RuntimeException("Invalid sortname:" + sortname);
        } else {
            this.sortname = sortname;
        }
    }

    public String getSortorder() {
        return this.sortorder;
    }

    public void setSortorder(String sortorder) {
        if (StringUtils.isNotEmpty(sortorder) && !COL_PATTERN.matcher(sortorder).matches()) {
            throw new RuntimeException("Invalid sortorder:" + this.sortname);
        } else {
            this.sortorder = sortorder;
        }
    }

    public String get_token() {
        return this._token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getProgramId() {
        return this.programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Long getLastUpdateLogin() {
        return this.lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getAttributeCategory() {
        return this.attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public String getAttribute1() {
        return this.attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return this.attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return this.attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return this.attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return this.attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return this.attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return this.attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return this.attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return this.attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return this.attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return this.attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return this.attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return this.attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return this.attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return this.attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }

    @JsonAnyGetter
    public Map getInnerMap() {
        return this.innerMap;
    }

    public Object getAttribute(String key) {
        return this.innerMap.get(key);
    }

    @JsonAnySetter
    public void setAttribute(String key, Object obj) {
        this.innerMap.put(key, obj);
    }
}


