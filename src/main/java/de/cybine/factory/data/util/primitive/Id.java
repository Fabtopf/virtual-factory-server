package de.cybine.factory.data.util.primitive;

import java.io.Serializable;
import java.util.Optional;

public interface Id<T> extends Serializable
{
    T getValue( );

    default Optional<T> findValue( )
    {
        return Optional.ofNullable(this.getValue());
    }
}
