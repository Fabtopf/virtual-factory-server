package de.cybine.factory.exception;

import de.cybine.quarkus.exception.TechnicalException;
import lombok.experimental.StandardException;

@StandardException
@SuppressWarnings("unused")
public class DatabaseMigrationException extends TechnicalException
{ }