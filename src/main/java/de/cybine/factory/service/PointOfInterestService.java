package de.cybine.factory.service;

import de.cybine.factory.data.poi.PointOfInterest;
import de.cybine.factory.data.poi.PointOfInterestEntity;
import de.cybine.factory.data.sensor.Sensor;
import de.cybine.quarkus.util.api.ApiFieldResolver;
import de.cybine.quarkus.util.api.GenericApiQueryService;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static de.cybine.factory.data.poi.PointOfInterestEntity_.*;

@Startup
@ApplicationScoped
@RequiredArgsConstructor
public class PointOfInterestService
{
    private final GenericApiQueryService<PointOfInterestEntity, PointOfInterest> service =
            GenericApiQueryService.forType(
            PointOfInterestEntity.class, PointOfInterest.class);

    private final ApiFieldResolver resolver;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerType(Sensor.class)
                     .withField("id", ID)
                     .withField("server_id", SERVER_ID)
                     .withField("world", WORLD)
                     .withField("location_x", X)
                     .withField("localtion_y", Y)
                     .withField("location_z", Z)
                     .withField("type", TYPE);
    }

    public List<PointOfInterest> fetch(ApiQuery query)
    {
        return this.service.fetch(query);
    }

    public Optional<PointOfInterest> fetchSingle(ApiQuery query)
    {
        return this.service.fetchSingle(query);
    }

    public <O> List<O> fetchOptions(ApiOptionQuery query)
    {
        return this.service.fetchOptions(query);
    }

    public List<ApiCountInfo> fetchTotal(ApiCountQuery query)
    {
        return this.service.fetchTotal(query);
    }
}
