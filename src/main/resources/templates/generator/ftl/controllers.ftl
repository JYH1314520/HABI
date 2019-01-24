package ${package}.controllers;

<#list import as e>
import ${e};
</#list>
import ${package}.entity.${dtoName};
import ${package}.service.${serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ${controllerName} extends BaseController{

    @Autowired
    private ${serviceName} service;


    @RequestMapping(value = "${queryUrl}")
    @ResponseBody
    public ResponseData query(${dtoName} entity, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,entity,page,pageSize));
    }

    @RequestMapping(value = "${submitUrl}")
    @ResponseBody
    public ResponseData update(@RequestBody List<${dtoName}> entity, BindingResult result, HttpServletRequest request){
        getValidator().validate(entity, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, entity));
    }

    @RequestMapping(value = "${removeUrl}")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<${dtoName}> entity){
        service.batchDelete(entity);
        return new ResponseData();
    }
    }