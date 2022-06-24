package com.ohol.pavel.contactsapi.rest.swagger;

import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromContextPath;

/**
 * Overrides default swagger controller.
 */
@Controller
@ApiIgnore
public class SwaggerController {

    /**
     * Dfeault api docs url.
     */
    public static final String DEFAULT_URL = "/v2/api-docs";

    /**
     * Supported media types.
     */
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    /**
     * DocumentationCache.
     */
    private final DocumentationCache documentationCache;

    /**
     * Model mapper.
     */
    private final ServiceModelToSwagger2Mapper mapper;

    /**
     * Serializer.
     */
    private final JsonSerializer jsonSerializer;

    /**
     * Constructs instance with given dependencies.
     * @param aDocumentationCache {@link DocumentationCache}.
     * @param aMapper {@link ServiceModelToSwagger2Mapper}.
     * @param aJsonSerializer {@link JsonSerializer}.
     */
    @Autowired
    public SwaggerController(
            final DocumentationCache aDocumentationCache,
            final ServiceModelToSwagger2Mapper aMapper,
            final JsonSerializer aJsonSerializer) {

        documentationCache = aDocumentationCache;
        mapper = aMapper;
        jsonSerializer = aJsonSerializer;
    }

    /**
     * Documentation endpoint.
     * @param swaggerGroup app docs group.
     * @param servletRequest servlet request.
     * @return documentaion presented via {@link Json}.
     */
    @RequestMapping(
            value = DEFAULT_URL,
            method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @PropertySourcedMapping(
            value = "${springfox.documentation.swagger.v2.path}",
            propertyKey = "springfox.documentation.swagger.v2.path")
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(
            @RequestParam(value = "group", required = false)
            final String swaggerGroup,
            final HttpServletRequest servletRequest) {

        String groupName = Optional
                .ofNullable(swaggerGroup)
                .orElse(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache
                .documentationByGroup(groupName);
        if (documentation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        UriComponents uriComponents = componentsFrom(
                servletRequest,
                swagger.getBasePath()
        );
        swagger.setBasePath(isNullOrEmpty(uriComponents.getPath()) ? "/"
                : uriComponents.getPath());
        swagger.setHost(servletRequest.getHeader("host"));
        return new ResponseEntity<>(jsonSerializer.toJson(swagger),
                HttpStatus.OK);
    }

    /**
     * Builds UriComponents by given request and base path.
     * @param request request.
     * @param basePath base path.
     * @return {@link UriComponents}.
     */
    private static UriComponents componentsFrom(
            final HttpServletRequest request,
            final String basePath) {

        ServletUriComponentsBuilder builder = fromServletMapping(
                request,
                basePath);


        UriComponents components = UriComponentsBuilder.fromHttpRequest(
                        new ServletServerHttpRequest(request))
                .build();

        String host = components.getHost();
        if (!hasText(host)) {
            return builder.build();
        }

        builder.host(host);
        builder.port(components.getPort());

        return builder.build();
    }

    /**
     * Builds ServletUriComponentsBuilder from request and base path.
     * @param request request
     * @param basePath base path
     * @return {@link ServletUriComponentsBuilder}.
     */
    private static ServletUriComponentsBuilder fromServletMapping(
            final HttpServletRequest request,
            final String basePath) {

        ServletUriComponentsBuilder builder = fromContextPath(request);

        builder.replacePath(prependForwardedPrefix(request, basePath));
        if (hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
            builder.path(request.getServletPath());
        }

        return builder;
    }

    /**
     * Care about forwarded prefix.
     * @param request request.
     * @param path base path.
     * @return prefixed header.
     */
    private static String prependForwardedPrefix(
            final HttpServletRequest request,
            final String path) {

        String prefix = request.getHeader("X-Forwarded-Prefix");
        if (prefix != null) {
            return prefix + path;
        } else {
            return path;
        }
    }
}
