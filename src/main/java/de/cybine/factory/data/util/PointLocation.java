package de.cybine.factory.data.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record PointLocation(@JsonProperty("x") double x, @JsonProperty("y") double y, @JsonProperty("z") double z)
        implements Serializable
{ }
