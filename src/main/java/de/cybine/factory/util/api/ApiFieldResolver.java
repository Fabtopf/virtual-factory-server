package de.cybine.factory.util.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.util.BiTuple;
import de.cybine.factory.util.api.permission.ApiContextConfig;
import de.cybine.factory.util.api.permission.ApiContextMapping;
import de.cybine.factory.util.api.permission.ApiPermission;
import de.cybine.factory.util.api.permission.ApiPermissionConfig;
import de.cybine.factory.util.datasource.DatasourceField;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ApiFieldResolver
{
    public static final String DEFAULT_CONTEXT = "default";

    private final ObjectMapper     objectMapper;
    private final SecurityIdentity securityIdentity;

    private final Map<String, ApiFieldResolverContext> contexts = new HashMap<>();

    private final Map<BiTuple<Type, String>, DatasourceField> fields = new HashMap<>();

    private final Map<Type, Type> representationTypes = new HashMap<>();

    private ApiPermissionConfig permissionConfig;

    @PostConstruct
    @SneakyThrows
    void setup( )
    {
        this.permissionConfig = this.objectMapper.readValue(
                this.getClass().getClassLoader().getResource("api-permissions.json"), ApiPermissionConfig.class);

        this.contexts.clear();
        for (ApiContextConfig context : this.permissionConfig.getContexts())
            System.out.println("ctx: " + this.getContext(context.getName()).getContextName());
    }

    public ApiFieldResolverContext getUserContext( )
    {
        return this.permissionConfig.getContextMappings()
                                    .stream()
                                    .filter(item -> this.securityIdentity.checkPermissionBlocking(
                                            ApiPermission.of(item.getPermission())))
                                    .sorted()
                                    .findFirst()
                                    .map(ApiContextMapping::getName)
                                    .flatMap(this::findContext)
                                    .orElseGet(this::getDefaultContext);
    }

    public ApiFieldResolverContext getDefaultContext( )
    {
        return this.getContext(ApiFieldResolver.DEFAULT_CONTEXT);
    }

    public ApiFieldResolverContext getContext(String name)
    {
        return this.contexts.computeIfAbsent(name, context -> new ApiFieldResolverContext(context, this.fields::get));
    }

    public Optional<ApiFieldResolverContext> findContext(String context)
    {
        return Optional.ofNullable(this.contexts.get(context));
    }

    public ApiFieldResolver registerField(Type dataType, String alias, DatasourceField field)
    {
        log.debug("Registering api-field: {}({})", dataType.getTypeName(), alias);

        this.fields.put(new BiTuple<>(this.findRepresentationType(dataType).orElse(dataType), alias), field);
        return this;
    }

    public ApiFieldResolverHelper registerTypeRepresentation(Type representationType, Type datasourceType)
    {
        this.representationTypes.put(representationType, datasourceType);
        return this.getTypeRepresentationHelper(representationType);
    }

    public ApiFieldResolverHelper getTypeRepresentationHelper(Type representationType)
    {
        return new ApiFieldResolverHelper(this, representationType);
    }

    public Optional<Type> findRepresentationType(Type type)
    {
        return Optional.ofNullable(this.representationTypes.get(type));
    }
}
