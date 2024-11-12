package py.com.cls.infrastructure.in.adapters.resources;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import py.com.cls.infrastructure.in.adapters.commons.Api;

@ApplicationPath(Api.PATH_SERVICE)
@OpenAPIDefinition(
        info = @Info(
                version = "1.0.0",
                title = "CLS API",
                description = "API for Customer loyalty",
                license = @License(name = "Simple License")
        )
)
public class ApplicationResource extends Application {}
