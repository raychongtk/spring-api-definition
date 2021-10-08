package codegen.api.definition;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;

/**
 * @author raychong
 */
public class HttpMethodParser {
    public static HttpMethod parse(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return HttpMethod.GET;
        if (method.isAnnotationPresent(PostMapping.class)) return HttpMethod.POST;
        if (method.isAnnotationPresent(PutMapping.class)) return HttpMethod.PUT;
        if (method.isAnnotationPresent(DeleteMapping.class)) return HttpMethod.DELETE;
        throw new Error("unknown http method");
    }
}
