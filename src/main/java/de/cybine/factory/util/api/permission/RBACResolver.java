package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.config.ApplicationConfig;
import de.cybine.factory.exception.TechnicalException;
import de.cybine.factory.util.FilePathHelper;
import io.quarkus.arc.Arc;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
@RequiredArgsConstructor
public class RBACResolver
{
    private final ObjectMapper      objectMapper;
    private final ApplicationConfig applicationConfig;

    private final Map<String, RBACRole> roles = new HashMap<>();

    @PostConstruct
    void setup( ) throws URISyntaxException
    {
        FilePathHelper.resolvePath(this.applicationConfig.paths().rbacPath()).ifPresent(this::reload);
    }

    public void reload(Path path)
    {
        try
        {
            JavaType type = this.objectMapper.getTypeFactory().constructParametricType(List.class, RBACRole.class);
            List<RBACRole> rbacRoles = this.objectMapper.readValue(path.toFile(), type);

            this.roles.clear();
            for (RBACRole role : rbacRoles)
                this.roles.put(role.getName(), role);
        }
        catch (IOException exception)
        {
            throw new TechnicalException("Could not load rbac-data", exception);
        }
    }

    public boolean hasPermission(String role, Permission permission)
    {
        RBACRole rbacRole = this.roles.get(role);
        if (rbacRole == null)
            return false;

        if (rbacRole.hasPermission(permission.getName()))
            return true;

        if (rbacRole.getInheritedRoles().isEmpty())
            return false;

        return rbacRole.getInheritedRoles()
                       .get()
                       .stream()
                       .map(this.roles::get)
                       .filter(Objects::nonNull)
                       .anyMatch(item -> item.hasPermission(permission.getName()));
    }

    public PermissionChecker toPermissionChecker( )
    {
        SecurityIdentity securityIdentity = Arc.container().select(SecurityIdentity.class).get();
        return permission -> Uni.createFrom()
                                .item(securityIdentity.getRoles()
                                                      .stream()
                                                      .anyMatch(item -> this.hasPermission(item, permission)));
    }
}
