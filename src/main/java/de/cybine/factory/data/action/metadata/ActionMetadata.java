package de.cybine.factory.data.action.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextId;
import de.cybine.factory.util.Views;
import de.cybine.factory.util.WithId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionMetadata implements Serializable, WithId<ActionMetadataId>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @JsonDeserialize(using = ActionMetadataId.Deserializer.class)
    private final ActionMetadataId id;

    @JsonProperty("namespace")
    private final String namespace;

    @JsonProperty("category")
    private final String category;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final ActionType type;

    @JsonProperty("contexts")
    @JsonView(Views.Extended.class)
    private final Set<ActionContext> contexts;

    public Optional<Set<ActionContext>> getContexts( )
    {
        return Optional.ofNullable(this.contexts);
    }

    @JsonProperty("context_ids")
    @JsonView(Views.Simple.class)
    private Optional<Set<ActionContextId>> getContextIds( )
    {
        return this.getContexts().map(items -> items.stream().map(WithId::getId).collect(Collectors.toSet()));
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
