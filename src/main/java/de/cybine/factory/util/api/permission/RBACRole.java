package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
public class RBACRole
{
    @NotNull
    @JsonProperty("name")
    private final String name;

    @JsonProperty("inherited_roles")
    private final List<String> inheritedRoles;

    @NotNull
    @JsonProperty("permissions")
    private final List<String> permissions;

    public boolean hasPermission(String permission)
    {
        return this.permissions.contains(permission);
    }

    public Optional<List<String>> getInheritedRoles( )
    {
        return Optional.ofNullable(this.inheritedRoles);
    }
}
