package com.mit.app;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Le on 2/16/17.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    private TypeResolver typeResolver;

    @Value("${system.test.signature}")
    private String testSign;

    @Bean
    public Docket api() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        Parameter sessionHeader = new ParameterBuilder()
                .name("Session")
                .description("Description of header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameterList.add(sessionHeader);

        Parameter sign = new ParameterBuilder()
                .name("Signature")
                .description("Description of header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(testSign)
                .required(true)
                .build();
        parameterList.add(sign);

        Parameter time = new ParameterBuilder()
                .name("Request-Time")
                .description("Time request")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(System.currentTimeMillis() + "")
                .required(true)
                .build();
        parameterList.add(time);


        Parameter api = new ParameterBuilder()
                .name("Api-Key")
                .description("Description of header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("api-key")
                .required(true)
                .build();
        parameterList.add(api);

        return new Docket(DocumentationType.SWAGGER_2).globalOperationParameters(parameterList)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .pathProvider(new BasePathAwareRelativePathProvider("/bq"))
//                .genericModelSubstitutes(ApiResponse.class)
//                .alternateTypeRules(
//                        newRule(typeResolver.resolve(ApiResponse.class,
//                                WildcardType.class),
//                                typeResolver.resolve(WildcardType.class)))
                ;
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "BeautyQueen REST API",
                "Prefix URL: http://115.79.45.86/bq/",
                "",
                "",
                "",
                "",
                "");
        return apiInfo;
    }

    class BasePathAwareRelativePathProvider extends AbstractPathProvider {
        private String basePath;

        public BasePathAwareRelativePathProvider(String basePath) {
            this.basePath = basePath;
        }

        @Override
        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
        }
    }
}
