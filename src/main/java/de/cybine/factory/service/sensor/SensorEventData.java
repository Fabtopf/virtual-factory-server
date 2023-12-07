package de.cybine.factory.service.sensor;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.cybine.factory.data.poi.PointOfInterestId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorEventData
{
    @JsonProperty("poi_id")
    private final PointOfInterestId poiId;

    @JsonProperty("action")
    private final String action;

    @JsonAnyGetter
    @JsonAnySetter
    private final Map<String, Object> data;
}
