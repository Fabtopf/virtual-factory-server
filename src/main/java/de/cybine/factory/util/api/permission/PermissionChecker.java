package de.cybine.factory.util.api.permission;

import io.smallrye.mutiny.Uni;

import java.security.Permission;
import java.util.function.Function;

@FunctionalInterface
public interface PermissionChecker extends Function<Permission, Uni<Boolean>>
{ }
