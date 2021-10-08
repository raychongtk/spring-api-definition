package codegen.api.definition;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author raychong
 */
@Component
public class ApiDefinitionCodeGenerator {
    @Autowired
    ListableBeanFactory listableBeanFactory;

    public List<ApiDefinition> getApiDefinitions() {
        List<ApiDefinition> apiDefinitions = new ArrayList<>();
        Map<String, Object> beans = listableBeanFactory.getBeansWithAnnotation(RestController.class);

        for (Object obj : beans.values()) {
            var apiDefinition = new ApiDefinition();
            Class<?> controller = obj.getClass();
            apiDefinition.webService = controller.getSimpleName();
            var parentPath = controller.getAnnotation(RequestMapping.class);
            for (Method method : controller.getDeclaredMethods()) {
                apiDefinition.webServiceApis.add(determineApiDefinition(method, parentPath));
            }
            apiDefinitions.add(apiDefinition);
        }

        return apiDefinitions;
    }

    private ApiDefinition.WebServiceApi determineApiDefinition(Method method, RequestMapping path) {
        var webServiceApi = new ApiDefinition.WebServiceApi();
        webServiceApi.method = HttpMethodParser.parse(method);
        webServiceApi.endpoint = parseEndpoint(method, path);
        webServiceApi.action = method.getName();
        webServiceApi.request = determineRequestBody(method);
        webServiceApi.response = determineResponseBody(method);
        return webServiceApi;
    }

    private String parseEndpoint(Method method, RequestMapping parentPath) {
        String path = "";
        if (parentPath != null) {
            path = parentPath.value()[0];
        }

        if (method.isAnnotationPresent(PostMapping.class)) return path + method.getAnnotation(PostMapping.class).value()[0];
        if (method.isAnnotationPresent(PutMapping.class)) return path + method.getAnnotation(PutMapping.class).value()[0];
        if (method.isAnnotationPresent(GetMapping.class)) return path + method.getAnnotation(GetMapping.class).value()[0];
        if (method.isAnnotationPresent(DeleteMapping.class)) return path + method.getAnnotation(DeleteMapping.class).value()[0];
        throw new Error("unknown request mapping");
    }

    private ApiDefinition.ApiBody determineRequestBody(Method method) {
        var apiBody = new ApiDefinition.ApiBody();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.getAnnotation(RequestBody.class) != null) {
                Class<?> request = parameter.getType();
                apiBody.name = request.getSimpleName();
                List<ApiDefinition.ApiParameter> apiParameters = parseParam(request.getDeclaredFields());
                apiBody.parameters.addAll(apiParameters);
            }
        }
        return apiBody;
    }

    private ApiDefinition.ApiBody determineResponseBody(Method method) {
        var apiBody = new ApiDefinition.ApiBody();
        Class<?> response = method.getReturnType();
        apiBody.name = response.getSimpleName();
        List<ApiDefinition.ApiParameter> apiParameters = parseParam(response.getDeclaredFields());
        apiBody.parameters.addAll(apiParameters);
        return apiBody;
    }

    private List<ApiDefinition.ApiParameter> parseParam(Field[] fields) {
        List<ApiDefinition.ApiParameter> params = new ArrayList<>();
        for (Field field : fields) {
            var apiParam = new ApiDefinition.ApiParameter();
            apiParam.name = field.getName();
            apiParam.type = field.getGenericType().getTypeName();
            apiParam.optional = field.getAnnotation(NotNull.class) == null;
            params.add(apiParam);
        }
        return params;
    }
}
