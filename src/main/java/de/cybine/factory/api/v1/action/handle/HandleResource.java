package de.cybine.factory.api.v1.action.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.service.action.ActionService;
import de.cybine.factory.service.action.ContextService;
import de.cybine.quarkus.api.response.ApiResponse;
import de.cybine.quarkus.exception.action.ActionProcessingException;
import de.cybine.quarkus.util.action.data.*;
import de.cybine.quarkus.util.api.ApiQueryHelper;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Authenticated
@ApplicationScoped
@RequiredArgsConstructor
public class HandleResource implements HandleApi
{
    private final ObjectMapper objectMapper;

    private final ActionService  actionService;
    private final ContextService contextService;

    private final ActionDataTypeRegistry typeRegistry;

    @Override
    public RestResponse<ApiResponse<String>> create(String namespace, String category, String name, String itemId)
    {
        return ApiResponse.<String>builder()
                          .statusCode(RestResponse.Status.CREATED.getStatusCode())
                          .value(this.actionService.beginWorkflow(namespace, category, name, itemId))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<ActionProcess>> terminate(String correlationId)
    {
        return this.process(correlationId, null, ActionService.TERMINATED_STATE, null);
    }

    @Override
    public RestResponse<ApiResponse<ActionProcess>> process(String correlationId, String eventId, String action,
            Map<String, Object> data)
    {
        ActionContext context = this.contextService.fetchByCorrelationId(correlationId).orElseThrow();
        ActionProcess currentState = this.actionService.fetchCurrentState(correlationId).orElseThrow();
        if (eventId != null && !Objects.equals(currentState.getEventId(), eventId))
            return ApiResponse.<ActionProcess>builder()
                              .statusCode(RestResponse.Status.CONFLICT.getStatusCode())
                              .build()
                              .transform(ApiQueryHelper::createResponse);

        ActionData<Object> actionData = null;
        if (data != null)
        {
            if (!data.containsKey("@type"))
                throw new IllegalArgumentException("@type must be defined when data-object is used");

            if (!data.containsKey("value"))
                throw new IllegalArgumentException("value must be defined when data-object is used");

            try
            {
                String typeName = (String) data.get("@type");
                JavaType dataType = this.typeRegistry.findType(typeName).orElseThrow();

                String serializedData = this.objectMapper.writeValueAsString(data.get("value"));
                actionData = new ActionData<>(typeName, this.objectMapper.readValue(serializedData, dataType));
            }
            catch (JsonProcessingException exception)
            {
                throw new ActionProcessingException("Could not process data", exception);
            }
        }

        ActionMetadata metadata = ActionMetadata.builder()
                                                .namespace(context.getNamespace())
                                                .category(context.getCategory())
                                                .name(context.getName())
                                                .action(action)
                                                .correlationId(correlationId)
                                                .createdAt(ZonedDateTime.now())
                                                .build();

        this.actionService.perform(Action.of(metadata, actionData));

        return ApiResponse.<ActionProcess>builder()
                          .value(this.actionService.fetchCurrentState(correlationId).orElseThrow())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<List<String>>> fetchAvailableActions(String correlationId)
    {
        return ApiResponse.<List<String>>builder()
                          .value(this.actionService.availableActions(correlationId)
                                                   .stream()
                                                   .map(ActionProcessorMetadata::getAction)
                                                   .toList())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }
}
