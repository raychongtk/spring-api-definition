package codegen.controller;

import codegen.api.definition.ApiDefinitionCodeGenerator;
import codegen.api.definition.ApiDefinitionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author raychong
 */
@RestController
public class ApiDefinitionController {
    @Autowired
    ApiDefinitionCodeGenerator apiDefinitionCodeGenerator;

    @GetMapping("/api/definition")
    public ApiDefinitionResponse apiDefinition() {
        var response = new ApiDefinitionResponse();
        response.apiDefinitions = apiDefinitionCodeGenerator.getApiDefinitions();
        return response;
    }
}
