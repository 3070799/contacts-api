package com.ohol.pavel.contactsapi.config.swagger;

import com.ohol.pavel.contactsapi.rest.swagger.SwaggerController;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

/**
 * Contains swagger configuration.
 */
@Configuration
public class SwaggerConfig {

    /** Returns configured swagger docket.
     * @return {@link Docket}.
     */
    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.ohol.pavel.contactsapi"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Lists.newArrayList(apiKey()));

    }

    /**
     * Generates swagger api info.
     *
     * @return {@link ApiInfo}.
     */
    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("REST API")
                .description("The application REST API")
                .version("0.0.1")
                .build();
    }

    /**
     * Generates swagger api key.
     *
     * @return {@link ApiKey}.
     */
    private static ApiKey apiKey() {
        return new ApiKey(
                "%token",
                AUTHORIZATION,
                "Header"
        );
    }

    /**
     * Overrides default swagger controller to be able to work behind proxy.
     *
     * @param environment        swagger env
     * @param documentationCache docs cache
     * @param mapper             mapper,
     * @param jsonSerializer     json serializer
     * @return {@link HandlerMapping}.
     */
    @Bean
    @Primary
    public HandlerMapping swagger2ControllerMapping(
            final Environment environment,
            final DocumentationCache documentationCache,
            final ServiceModelToSwagger2Mapper mapper,
            final JsonSerializer jsonSerializer) {
        return new PropertySourcedRequestMappingHandlerMapping(
                environment,
                new SwaggerController(
                        documentationCache,
                        mapper,
                        jsonSerializer
                )
        );
    }
}
