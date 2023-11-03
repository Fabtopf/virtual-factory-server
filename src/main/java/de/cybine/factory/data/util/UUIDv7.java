package de.cybine.factory.data.util;

import com.fasterxml.uuid.Generators;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor(staticName = "of")
public class UUIDv7
{
    private final UUID id;

    public static UUIDv7 create()
    {
        return new UUIDv7(UUIDv7.generate());
    }

    public static UUID generate()
    {
        return Generators.timeBasedEpochGenerator().generate();
    }
}
