package dev.danielarrais.votingsystem.api.config;

import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIT {
    @Container
    protected static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.18")
            .withUrlParam("TC_DAEMON", "true")
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

    public static <T> ParameterizedTypeReference<ArrayList<T>> getTypeList(Class<T> tClass) {
        return new ParameterizedTypeReference<ArrayList<T>>() {};
    }
}
