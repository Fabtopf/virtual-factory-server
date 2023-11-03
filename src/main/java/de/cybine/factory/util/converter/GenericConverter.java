package de.cybine.factory.util.converter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public class GenericConverter<I, O> implements Converter<I, O>
{
    private final Class<I> inputType;
    private final Class<O> outputType;

    @Getter(AccessLevel.NONE)
    private final BiFunction<I, ConversionHelper, O> conversionFunction;

    @Override
    public O convert(I input, ConversionHelper helper)
    {
        return this.conversionFunction.apply(input, helper);
    }
}
