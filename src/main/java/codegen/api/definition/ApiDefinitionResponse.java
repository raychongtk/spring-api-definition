package codegen.api.definition;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raychong
 */
public class ApiDefinitionResponse {
    @NotNull
    public List<ApiDefinition> apiDefinitions = new ArrayList<>();
}
