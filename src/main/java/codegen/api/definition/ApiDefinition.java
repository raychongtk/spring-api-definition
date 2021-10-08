package codegen.api.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author raychong
 */
public class ApiDefinition {
    public String webService;

    public List<WebServiceApi> webServiceApis = new ArrayList<>();

    public static class WebServiceApi {
        public HttpMethod method;
        public String endpoint;
        public String action;
        public ApiBody request;
        public ApiBody response;
    }

    public static class ApiBody {
        public String name;
        public List<ApiParameter> parameters = new ArrayList<>();
    }

    public static class ApiParameter {
        public String name;
        public String type;
        public Boolean optional;
    }
}
