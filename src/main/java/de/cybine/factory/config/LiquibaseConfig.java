package de.cybine.factory.config;

import de.cybine.factory.exception.DatabaseMigrationException;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSetStatus;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Startup
@Dependent
@RequiredArgsConstructor
public class LiquibaseConfig
{
    private final LiquibaseFactory factory;

    @PostConstruct
    public void setup( )
    {
        try
        {
            this.checkMigrations();
        }
        catch (LiquibaseException exception)
        {
            throw new DatabaseMigrationException(exception);
        }
    }

    private void checkMigrations( ) throws LiquibaseException
    {
        try (Liquibase liquibase = this.factory.createLiquibase())
        {
            Contexts contexts = this.factory.createContexts();
            LabelExpression labels = this.factory.createLabels();

            liquibase.validate();
            liquibase.update(contexts, labels);

            liquibase.getChangeSetStatuses(contexts, labels).forEach(this::logMigration);
        }
    }

    private void logMigration(ChangeSetStatus status)
    {
    }
}
