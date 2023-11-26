package dev.danielarrais.votingsystem.api.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIT {
    @Container
    protected static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.18")
            .withUrlParam("serverTimezone", "UTC")
            .withDatabaseName("voting-system")
            .withCopyFileToContainer(forClasspathResource("db/changelog/changes"), "/docker-entrypoint-initdb.d")
            .withFileSystemBind("docker/db", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE);

    @DynamicPropertySource
    public static void replaceDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name=", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.url=", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", mySQLContainer::getUsername);
        registry.add("spring.datasource.password=", mySQLContainer::getPassword);
    }

    @BeforeAll
    public static void testContainerSetup() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void testContainerTearDown() {
        mySQLContainer.stop();
    }
}
