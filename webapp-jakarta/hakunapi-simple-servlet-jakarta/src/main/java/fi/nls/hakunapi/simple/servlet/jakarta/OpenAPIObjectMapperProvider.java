package fi.nls.hakunapi.simple.servlet.jakarta;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.v3.core.util.Json;

@Provider
@Produces(MediaTypes.APPLICATION_OPENAPI_V3)
public class OpenAPIObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return Json.mapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

}