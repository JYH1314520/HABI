package ${package}.service;

<#list import as e>
import ${e};
</#list>
import ${package}.entity.${dtoName};

public interface ${serviceName} extends IBaseService<${dtoName}>, ProxySelf<${serviceName}>{

}