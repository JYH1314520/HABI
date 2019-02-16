package ${package}.mapper;

<#list import as e>
import ${e};
</#list>
import ${package}.entity.${dtoName};
@org.apache.ibatis.annotations.Mapper
public interface ${mapperName} extends Mapper<${dtoName}>{

}