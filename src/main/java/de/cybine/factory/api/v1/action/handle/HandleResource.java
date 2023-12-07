package de.cybine.factory.api.v1.action.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextMetadata;
import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.data.action.process.ActionProcessMetadata;
import de.cybine.factory.exception.action.ActionProcessingException;
import de.cybine.factory.service.action.data.ActionData;
import de.cybine.factory.service.action.data.ActionDataTypeRegistry;
import de.cybine.factory.service.action.ActionService;
import de.cybine.factory.service.action.ContextService;
import de.cybine.factory.util.api.response.ApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class HandleResource implements HandleApi
{
    private final ObjectMapper objectMapper;

    private final ActionService  actionService;
    private final ContextService contextService;

    private final ActionDataTypeRegistry typeRegistry;

    @Override
    public RestResponse<ApiResponse<ActionContext>> create(String namespace, String category, String name,
            String itemId)
    {
        ActionContextMetadata metadata = ActionContextMetadata.builder()
                                                              .namespace(namespace)
                                                              .category(category)
                                                              .name(name)
                                                              .itemId(itemId)
                                                              .build();

        return ApiResponse.<ActionContext>builder()
                          .status(RestResponse.Status.CREATED)
                          .value(this.actionService.createContext(metadata))
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<Void>> terminate(UUID correlationId)
    {
        ActionContext context = this.contextService.fetchByCorrelationId(correlationId).orElseThrow();
        this.actionService.terminateContext(context.getId());

        return ApiResponse.<Void>builder().build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionProcess>> process(UUID correlationId, UUID eventId, String action,
            Map<String, Object> data)
    {
        ActionContext context = this.contextService.fetchByCorrelationId(correlationId).orElseThrow();
        ActionProcess currentState = this.actionService.fetchCurrentState(context.getId()).orElseThrow();
        if (eventId != null && !Objects.equals(currentState.getEventId(), eventId.toString()))
            return ApiResponse.<ActionProcess>builder().status(RestResponse.Status.CONFLICT).build().toResponse();

        ActionData<?> actionData = null;
        if (data != null)
        {
            if (!data.containsKey("@type"))
                throw new IllegalArgumentException("@type must be defined when data-object is used");

            if (!data.containsKey("value"))
                throw new IllegalArgumentException("value must be defined when data-object is used");

            try
            {
                JavaType dataType = this.typeRegistry.findType((String) data.get("@type")).orElseThrow();

                String serializedData = this.objectMapper.writeValueAsString(data.get("value"));
                actionData = ActionData.of(this.objectMapper.readValue(serializedData, dataType));
            }
            catch (JsonProcessingException exception)
            {
                throw new ActionProcessingException("Could not process data", exception);
            }
        }

        this.actionService.process(ActionProcessMetadata.builder()
                                                        .contextId(context.getId())
                                                        .status(action)
                                                        .createdAt(ZonedDateTime.now())
                                                        .data(actionData)
                                                        .build());

        return ApiResponse.<ActionProcess>builder()
                          .value(this.actionService.fetchCurrentState(context.getId()).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<String>>> fetchAvailableActions(UUID correlationId)
    {
        return ApiResponse.<List<String>>builder()
                          .value(this.actionService.fetchAvailableActions(correlationId))
                          .build()
                          .toResponse();
    }
}
