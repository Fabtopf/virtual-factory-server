package de.cybine.factory.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
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

    @NotNull @NotBlank
    @WithName("app-id")
    String appId( );
}
