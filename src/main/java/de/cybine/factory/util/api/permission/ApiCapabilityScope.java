package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiCapabilityScope
{
    ALL("all"), OWN("own"), OTHERS("others");

    @JsonValue
    private final String scope;

    public static Optional<ApiCapabilityScope> findByScope(String scope)
    {
        if (scope == null)
            return Optional.empty();

        return Arrays.stream(ApiCapabilityScope.values()).filter(item -> item.getScope().equals(scope)).findAny();
    }
}
