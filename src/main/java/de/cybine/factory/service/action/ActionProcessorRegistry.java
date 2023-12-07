package de.cybine.factory.service.action;

import de.cybine.factory.exception.action.DuplicateProcessorDefinitionException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ActionProcessorRegistry
{
    private final Map<ActionProcessorMetadata, ActionProcessor<?>> processors = new HashMap<>();

    public void registerProcessors(List<ActionProcessor<?>> processors)
    {
        for (ActionProcessor<?> processor : processors)
            this.registerProcessor(processor);
    }

    public void registerProcessor(ActionProcessor<?> processor)
    {
        ActionProcessorMetadata metadata = processor.getMetadata();
        if (this.processors.containsKey(metadata))
            throw new DuplicateProcessorDefinitionException(metadata.asString());

        log.debug("Registering action-processor: {}", metadata.asString());

        this.processors.put(processor.getMetadata(), processor);
    }

    public void removeProcessor(ActionProcessorMetadata metadata)
    {
        this.processors.remove(metadata);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<ActionProcessor<T>> findProcessor(ActionProcessorMetadata metadata)
    {
        log.debug("Searching action-processor: {}", metadata.asString());

        return Optional.ofNullable((ActionProcessor<T>) this.processors.get(metadata));
    }

    public List<String> getPossibleActions(String namespace, String category, String name, String status)
    {
        return this.processors.keySet()
                              .stream()
                              .filter(metadata -> metadata.getNamespace().equals(namespace))
                              .filter(metadata -> metadata.getCategory().equals(category))
                              .filter(metadata -> metadata.getName().equals(name))
                              .filter(metadata -> metadata.getFromStatus().equals(status))
                              .map(ActionProcessorMetadata::getToStatus)
                              .toList();
    }
}
