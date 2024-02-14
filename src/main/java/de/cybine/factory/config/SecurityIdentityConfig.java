package de.cybine.factory.config;

import de.cybine.factory.config.AuthenticationConfig.Mechanism;
import de.cybine.quarkus.util.api.permission.PermissionChecker;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.control.ActivateRequestContext;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@Setter
@Dependent
@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
public class SecurityIdentityConfig implements Supplier<SecurityIdentity>
{
    private SecurityIdentity identity;

    private Mechanism         mechanism;
    private PermissionChecker permissionChecker;

    @Override
    @ActivateRequestContext
    public SecurityIdentity get( )
    {
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(this.identity)
                                                                         .addPermissionChecker(this.permissionChecker);

        if (this.identity.isAnonymous())
            return builder.build();

        return switch (this.mechanism)
        {
            case API_TOKEN ->
            {
                log.debug("Augmenting api-token");
                // TODO: custom api-token logic
                yield builder.build();
            }

            case OIDC -> builder.build();
        };
    }
}
