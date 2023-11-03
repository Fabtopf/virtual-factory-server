package de.cybine.factory.exception;

import de.cybine.factory.util.BiTuple;
import de.cybine.factory.util.api.response.ErrorResponse;
import lombok.AccessLevel;
import lombok.Getter;
import org.jboss.resteasy.reactive.RestResponse.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuppressWarnings("unused")
public class ServiceException extends RuntimeException
{
    protected final String code;
    protected final Status status;

    @Getter(AccessLevel.NONE)
    protected final List<BiTuple<String, Object>> data = new ArrayList<>();

    public ServiceException(String code, Status status, String message)
    {
        this(message, status, code, null);
    }

    public ServiceException(String code, Status status, String message, Throwable cause)
    {
        super(message, cause);

        this.code = code;
        this.status = status;
    }

    @SuppressWarnings("unchecked")
    public <T extends ServiceException> T addData(String key, Object value)
    {
        if (key == null || value == null)
            throw new IllegalArgumentException("Key and value must not be null");

        this.data.add(new BiTuple<>(key, value));
        return (T) this;
    }

    public ErrorResponse toResponse( )
    {
        return ErrorResponse.builder()
                            .code(this.code)
                            .message(this.getMessage())
                            .data(this.data.stream().collect(Collectors.toMap(BiTuple::first, BiTuple::second)))
                            .build();
    }
}
