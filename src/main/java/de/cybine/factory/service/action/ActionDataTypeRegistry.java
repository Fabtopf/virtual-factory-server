package de.cybine.factory.service.action;

import com.fasterxml.jackson.databind.JavaType;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ActionDataTypeRegistry
{
    private final Map<String, JavaType> dataTypes = new HashMap<>();

    public void registerType(String name, JavaType type)
    {
        this.dataTypes.put(name, type);
    }

    public Optional<JavaType> findType(String name)
    {
        return Optional.ofNullable(this.dataTypes.get(name));
    }

    public Optional<String> findTypeName(JavaType type)
    {
        return this.dataTypes.entrySet()
                             .stream()
                             .filter(entry -> entry.getValue().equals(type))
                             .map(Map.Entry::getKey)
                             .findFirst();
    }
}
