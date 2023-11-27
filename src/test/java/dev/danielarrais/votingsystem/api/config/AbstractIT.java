package dev.danielarrais.votingsystem.api.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {

    @Autowired
    protected TestRestTemplate template;

    @Container
    @ServiceConnection
    protected static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.18")
            .withUrlParam("serverTimezone", "UTC")
            .withDatabaseName("voting-system")
            .withCopyFileToContainer(forClasspathResource("db/changelog/changes"), "/docker-entrypoint-initdb.d");
}
