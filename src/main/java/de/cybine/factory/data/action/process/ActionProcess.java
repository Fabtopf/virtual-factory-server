package de.cybine.factory.data.action.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextId;
import de.cybine.factory.data.util.UUIDv7;
import de.cybine.factory.service.action.ActionData;
import de.cybine.factory.util.Views;
import de.cybine.factory.util.WithId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionProcess implements Serializable, WithId<ActionProcessId>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private final ActionProcessId id;

    @Builder.Default
    @JsonProperty("event_id")
    private final String eventId = UUIDv7.generate().toString();

    @JsonProperty("context_id")
    @JsonView(Views.Simple.class)
    private final ActionContextId contextId;

    @JsonProperty("context")
    @JsonView(Views.Extended.class)
    private final ActionContext context;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("priority")
    private final Integer priority;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("creator_id")
    private final String creatorId;

    @JsonProperty("created_at")
    private final ZonedDateTime createdAt;

    @JsonProperty("due_at")
    private final ZonedDateTime dueAt;

    @JsonProperty("data")
    private final ActionData<?> data;

    public Optional<Integer> getPriority( )
    {
        return Optional.ofNullable(this.priority);
    }

    public Optional<ActionContext> getContext( )
    {
        return Optional.ofNullable(this.context);
    }

    public Optional<String> getDescription( )
    {
        return Optional.ofNullable(this.description);
    }

    public Optional<String> getCreatorId( )
    {
        return Optional.ofNullable(this.creatorId);
    }

    public Optional<ZonedDateTime> getDueAt( )
    {
        return Optional.ofNullable(this.dueAt);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<ActionData<T>> getData( )
    {
        return Optional.ofNullable((ActionData<T>) this.data);
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;

        if(this.getClass() != other.getClass())
            return false;

        WithId<?> that = ((WithId<?>) other);
        if (this.findId().isEmpty() || that.findId().isEmpty())
            return false;

        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode( )
    {
        return this.findId().map(Object::hashCode).orElse(0);
    }
}
