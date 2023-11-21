package dev.danielarrais.votingsystem.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles(value = "end-to-end-test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.main.allow-bean-definition-overriding=true"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIT.DockerPostgresDataSourceInitializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIT {

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.18")
            .withUrlParam("TC_DAEMON", "true")
            .withFileSystemBind("docker/db", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE);

    static {
        mySQLContainer.start();
    }

    public static class DockerPostgresDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Assertions.assertNotNull(applicationContext);
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true",
                    "spring.datasource.driver-class-name=" + mySQLContainer.getDriverClassName(),
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword()
            );
        }
    }
}
