package de.cybine.factory.config;

import de.cybine.quarkus.util.api.permission.PermissionChecker;
import de.cybine.quarkus.util.api.permission.RBACResolver;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.quarkus.oidc.runtime.OidcAuthenticationMechanism;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.smallrye.jwt.runtime.auth.JWTAuthMechanism;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Instance;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Priority(1)
@Alternative
@ApplicationScoped
@RequiredArgsConstructor
public class AuthenticationConfig implements HttpAuthenticationMechanism, SecurityIdentityAugmentor
{
    public static final Mechanism DEFAULT_MECHANISM = Mechanism.OIDC;

    private final RBACResolver rbacResolver;

    private final JWTAuthMechanism            jwtMechanism;
    private final OidcAuthenticationMechanism oidcMechanism;

    private final Instance<SecurityIdentityConfig> securityIdentityConfig;

    private Mechanism mechanism;

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager)
    {
        return this.selectMechanism(context).authenticate(context, identityProviderManager);
    }

    @Override
    public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context)
    {
        return this.selectMechanism(context).getCredentialTransport(context);
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context)
    {
        return this.oidcMechanism.getChallenge(context);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes( )
    {
        Set<Class<? extends AuthenticationRequest>> types = new HashSet<>();
        types.addAll(this.jwtMechanism.getCredentialTypes());
        types.addAll(this.oidcMechanism.getCredentialTypes());

        return types;
    }

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context)
    {
        PermissionChecker permissionChecker = this.rbacResolver.toPermissionChecker();
        return context.runBlocking(this.securityIdentityConfig.get()
                                                              .mechanism(this.mechanism)
                                                              .permissionChecker(permissionChecker)
                                                              .identity(identity));
    }

    private HttpAuthenticationMechanism selectMechanism(RoutingContext context)
    {
        String authHeader = context.request().getHeader(HttpHeaderNames.AUTHORIZATION);
        this.mechanism = this.getMechanism(authHeader);
        return switch (this.mechanism)
        {
            case API_TOKEN -> this.jwtMechanism;
            case OIDC -> this.oidcMechanism;
        };
    }

    private Mechanism getMechanism(String authorizationHeader)
    {
        if (authorizationHeader != null && !authorizationHeader.isBlank())
        {
            String authType = authorizationHeader.split(" ")[ 0 ].toLowerCase();
            return Mechanism.findByName(authType).orElse(AuthenticationConfig.DEFAULT_MECHANISM);
        }

        return AuthenticationConfig.DEFAULT_MECHANISM;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Mechanism
    {
        OIDC("bearer"), API_TOKEN("token");

        private final String name;

        public static Optional<Mechanism> findByName(String name)
        {
            if (name == null)
                return Optional.empty();

            return Arrays.stream(Mechanism.values()).filter(item -> item.getName().equals(name)).findAny();
        }
    }
}
