package de.cybine.factory.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@UtilityClass
public class FilePathHelper
{
    public static Optional<Path> resolvePath(String path) throws URISyntaxException
    {
        if (path.startsWith("%resources%/"))
        {
            URL resourceUrl = FilePathHelper.class.getClassLoader().getResource(path.replace("%resources%/", ""));
            if (resourceUrl == null)
            {
                log.warn("Cloud not find resource-path '{}'. Please consider configuring custom a path.", path);
                return Optional.empty();
            }

            return Optional.of(Path.of(resourceUrl.toURI()));
        }

        return Optional.of(Path.of(path));
    }

    public static Optional<String> tryRead(Path path)
    {
        try
        {
            return Optional.of(Files.readString(path));
        }
        catch (IOException e)
        {
            return Optional.empty();
        }
    }
}
