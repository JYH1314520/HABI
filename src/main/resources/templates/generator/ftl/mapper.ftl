package ${package}.mapper;

<#list import as e>
import ${e};
</#list>
import ${package}.entity.${dtoName};

public interface ${mapperName} extends Mapper<${dtoName}>{

}