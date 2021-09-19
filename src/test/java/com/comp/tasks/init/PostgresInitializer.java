package com.innowise.hrm.employeemanagement.init;

import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String IMAGE = "postgres:latest";
    private static final String TEST_DATABASE = "employee_management_test";
    private static final String USER_NAME = "employee_test";
    private static final String PASSWORD = "employee_test";
    private static final Integer PORT = 5432;

    private final PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>(IMAGE)
            .withReuse(true)
            .withDatabaseName(TEST_DATABASE)
            .withUsername(USER_NAME)
            .withPassword(PASSWORD)
            .withExposedPorts(PORT);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        postgresContainer.start();
        postgresContainer.getMappedPort(PORT);

        //just for check dynamic ip DB
        ConnectionFactoryOptions options = PostgreSQLR2DBCDatabaseContainer.getOptions(
                postgresContainer
        );

        TestPropertyValues.of(
                String.format(
                        "database.port=%s",
                        postgresContainer.getMappedPort(PORT))
        ).applyTo(applicationContext.getEnvironment());
    }
}
