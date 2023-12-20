package de.cybine.factory.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@StaticInitSafe
@SuppressWarnings("unused")
@ConfigMapping(prefix = "application")
public interface ApplicationConfig
{
    @NotNull @NotBlank
    @WithName("base-url")
    String baseUrl( );

    @NotNull @NotNull
    @WithName("app-id")
    String appId( );

    @WithName("converter")
    Converter converter( );

    @WithName("paths")
    FilePaths paths( );

    interface Converter
    {
        @WithDefault("false")
        @WithName("allow-multi-level-relations")
        boolean allowMultiLevelRelations( );
    }

    interface FilePaths
    {
        @WithName("rbac-path")
        @WithDefault("%resources%/rbac.json")
        String rbacPath( );

        @WithName("api-permissions-path")
        @WithDefault("%resources%/api-permissions.json")
        String apiPermissionsPath( );
    }
}
