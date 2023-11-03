package de.cybine.factory.util.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T>
{
    @JsonIgnore
    @Builder.Default
    private final Status status = Status.OK;

    @JsonProperty("value")
    private final T value;

    @With
    @JsonProperty("self")
    private final ApiResourceInfo self;

    @JsonProperty("resources")
    private final List<ApiResourceDefinition> resources;

    public Optional<ApiResourceInfo> getSelf( )
    {
        return Optional.ofNullable(this.self);
    }

    @JsonIgnore
    public ResponseBuilder<ApiResponse<T>> toResponseBuilder( )
    {
        ResponseBuilder<ApiResponse<T>> builder = ResponseBuilder.create(this.status, this);

        this.getSelf().map(ApiResourceInfo::getHref).map(URI::create).ifPresent(builder::location);

        return builder;
    }

    @JsonIgnore
    public RestResponse<ApiResponse<T>> toResponse( )
    {
        return this.toResponseBuilder().build();
    }
}
