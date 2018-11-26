package com.habi.boot.system.base.utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class QueryFilter implements Serializable {
    private Example example = null;
    private Criteria criteria = null;
    private Integer page;
    private Integer pageSize;
    private Integer draw;
    private HttpServletRequest request;
    private boolean nosaas = true;
    private String saasId;

    public void setSaasId(String saasId) {
        this.saasId = saasId;
    }

    private QueryFilter() {
    }

    public QueryFilter(Class<?> entityClass) {
        this.example = new Example(entityClass);
        this.criteria = this.example.createCriteria();
    }

    private void orderBy(HttpServletRequest request, String name, String value) {
        if (name.contains("order") && name.contains("dir")) {
            String num = request.getParameter(name.replace("dir", "column"));
            String data = request.getParameter("columns[" + num + "][data]");
            if (data != null && !"".equals(data)) {
                this.example.setOrderByClause(data + " " + value);
            }
        }

    }

    public QueryFilter or(String condition, String value) {
        Criteria newcriteria = this.example.createCriteria();
        this.example.or(this.addCriteria(condition, value, newcriteria));
        return this;
    }

    public QueryFilter(Class<?> entityClass, HashMap<String, String> requestMap) {
        this.example = new Example(entityClass);
        this.criteria = this.example.createCriteria();
        if (requestMap != null && requestMap.size() > 0) {
            Set<Entry<String, String>> entrySet = requestMap.entrySet();
            Iterator iterator = entrySet.iterator();

            while(true) {
                while(true) {
                    String name;
                    String value;
                    do {
                        if (!iterator.hasNext()) {
                            return;
                        }

                        Entry<String, String> next = (Entry)iterator.next();
                        name = (String)next.getKey();
                        value = (String)next.getValue();
                    } while(!StringUtils.isNotBlank(value));

                    if (name.equalsIgnoreCase("start")) {
                        Integer start = Integer.parseInt(value);
                        String length = (String)requestMap.get("length");
                        this.setPage(start / Integer.parseInt(length) + 1);
                    } else if (name.equalsIgnoreCase("length")) {
                        this.setPageSize(Integer.parseInt(value));
                    } else if (name.equalsIgnoreCase("draw")) {
                        this.setDraw(Integer.parseInt(value));
                    } else if (!name.contains("_like") && !name.contains("_LIKE")) {
                        if (name.contains("_")) {
                            try {
                                this.addCriteria(name, value, this.criteria);
                            } catch (RuntimeException var10) {
                                ;
                            }
                        }
                    } else {
                        String[] strArr = name.split("_");

                        try {
                            this.criteria.andLike(strArr[0], "%" + value + "%");
                        } catch (RuntimeException var11) {
                            ;
                        }
                    }
                }
            }
        }
    }

    public QueryFilter(Class<?> entityClass, HttpServletRequest request) {
        this.request = request;
        this.example = new Example(entityClass);
        this.criteria = this.example.createCriteria();
        Enumeration names = request.getParameterNames();

        while(true) {
            String name;
            String value;
            do {
                if (!names.hasMoreElements()) {
                    return;
                }

                name = (String)names.nextElement();
                value = StringUtils.trim(request.getParameter(name));
            } while(!StringUtils.isNotBlank(value));

            if (name.equalsIgnoreCase("start")) {
                Integer start = Integer.parseInt(value);
                String length = request.getParameter("length");
                this.setPage(start / Integer.parseInt(length) + 1);
            } else if (name.equalsIgnoreCase("length")) {
                this.setPageSize(Integer.parseInt(value));
            } else if (name.equalsIgnoreCase("draw")) {
                this.setDraw(Integer.parseInt(value));
            } else if (!name.contains("_like") && !name.contains("_LIKE")) {
                if (name.contains("_")) {
                    try {
                        this.addCriteria(name, value, this.criteria);
                    } catch (RuntimeException var8) {
                        ;
                    }
                }
            } else {
                String[] strArr = name.split("_");

                try {
                    this.criteria.andLike(strArr[0], "%" + value + "%");
                } catch (RuntimeException var9) {
                    ;
                }
            }

            this.orderBy(request, name, value);
        }
    }

    private Criteria addCriteria(String condition, Object value, Criteria criteria) {
        String[] strArr;
        if (!condition.contains("_notlike") && !condition.contains("_NOTLIKE")) {
            if (!condition.contains("_like") && !condition.contains("_LIKE")) {
                String v;
                String[] vArr;
                StringBuffer sb;
                int i;
                if (!condition.contains("_in") && !condition.contains("_IN")) {
                    if (!condition.contains("_notin") && !condition.contains("_NOTIN")) {
                        if (!condition.contains("_isnull") && !condition.contains("_ISNULL")) {
                            if (!condition.contains("_isnotnull") && !condition.contains("_ISNOTNULL")) {
                                if (condition.contains("_EQ")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + "=", value);
                                } else if (condition.contains("_GT")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + ">", value);
                                } else if (condition.contains("_LT")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + "<", value);
                                } else if (condition.contains("_NEQ")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + "!=", value);
                                } else if (condition.contains("_GET")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + ">=", value);
                                } else if (condition.contains("_LET")) {
                                    strArr = condition.split("_");
                                    criteria.andCondition(strArr[0] + "<=", value);
                                } else {
                                    criteria.andCondition(condition, value);
                                }
                            } else {
                                strArr = condition.split("_");
                                criteria.andIsNotNull(strArr[0]);
                            }
                        } else {
                            strArr = condition.split("_");
                            criteria.andIsNull(strArr[0]);
                        }
                    } else {
                        strArr = condition.split("_");
                        if (value instanceof String) {
                            v = ((String)value).trim();
                            vArr = v.split(",");
                            sb = new StringBuffer();

                            for(i = 0; i < vArr.length; ++i) {
                                sb.append("'" + vArr[i] + "'");
                                if (i < vArr.length - 1) {
                                    sb.append(",");
                                }
                            }

                            criteria.andCondition(" " + strArr[0] + " not in ( " + sb.toString() + " )");
                        } else {
                            criteria.andNotIn(strArr[0], (List)value);
                        }
                    }
                } else {
                    strArr = condition.split("_");
                    if (value instanceof String) {
                        v = ((String)value).trim();
                        vArr = v.split(",");
                        sb = new StringBuffer();

                        for(i = 0; i < vArr.length; ++i) {
                            sb.append("'" + vArr[i] + "'");
                            if (i < vArr.length - 1) {
                                sb.append(",");
                            }
                        }

                        criteria.andCondition(" " + strArr[0] + " in ( " + sb.toString() + " )");
                    } else {
                        criteria.andIn(strArr[0], (List)value);
                    }
                }
            } else {
                strArr = condition.split("_");
                criteria.andLike(strArr[0], value + "");
            }
        } else {
            strArr = condition.split("_");
            criteria.andNotLike(strArr[0], value + "");
        }

        return criteria;
    }

    public QueryFilter addFilter(String condition, Object value) {
        this.addCriteria(condition, value, this.criteria);
        return this;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getDraw() {
        return this.draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public void setOrderby(String orderby) {
        this.example.setOrderByClause(orderby);
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Example getExample() {
        return this.example;
    }

    public QueryFilter setNosaas() {
        this.nosaas = false;
        return this;
    }
}


