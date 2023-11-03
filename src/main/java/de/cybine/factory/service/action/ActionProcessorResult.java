package de.cybine.factory.service.action;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionProcessorResult<T>
{
    private final T data;

    @Accessors(fluent = true)
    private final boolean hasData;

    public Optional<T> getData( )
    {
        return Optional.ofNullable(this.data);
    }

    public static ActionProcessorResult<Void> empty( )
    {
        return new ActionProcessorResult<>(null, false);
    }

    public static <T> ActionProcessorResult<T> of(T data)
    {
        return new ActionProcessorResult<>(data, true);
    }
}
