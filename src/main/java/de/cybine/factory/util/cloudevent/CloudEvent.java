package de.cybine.factory.util.cloudevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudEvent
{
    @Builder.Default
    @JsonProperty("specversion")
    private final String specVersion = "1.0";

    @JsonProperty("id")
    private final String id;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("subject")
    private final String subject;

    @JsonProperty("source")
    private final String source;

    @JsonProperty("time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private final ZonedDateTime time;

    @JsonProperty("correlation-id")
    private final String correlationId;

    @JsonProperty("priority")
    private final Integer priority;

    @JsonProperty("datacontenttype")
    private final String contentType;

    @Valid
    @JsonProperty(value = "data")
    @JsonSerialize(converter = CloudEventDataSerializer.class)
    @JsonDeserialize(converter = CloudEventDataDeserializer.class)
    private final CloudEventData<?> data;

    public Optional<String> getSubject( )
    {
        return Optional.ofNullable(this.subject);
    }

    public Optional<String> getCorrelationId( )
    {
        return Optional.ofNullable(this.correlationId);
    }

    public Optional<Integer> getPriority( )
    {
        return Optional.ofNullable(this.priority);
    }

    public Optional<String> getContentType( )
    {
        return Optional.ofNullable(this.contentType);
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T> Optional<CloudEventData<T>> getData( )
    {
        return Optional.ofNullable((CloudEventData<T>) this.data);
    }
}
