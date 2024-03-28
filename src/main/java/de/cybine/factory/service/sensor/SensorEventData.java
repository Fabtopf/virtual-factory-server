package de.cybine.factory.service.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("reference_id")
    private final String referenceId;

    @JsonProperty("action")
    private final String action;

    @JsonProperty("data")
    private final Map<String, Object> data;
}
