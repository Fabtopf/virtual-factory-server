package de.cybine.factory.util;

import java.util.Optional;

public interface WithId<T>
{
    T getId( );

    default Optional<T> findId( )
    {
        return Optional.ofNullable(this.getId());
    }
}
