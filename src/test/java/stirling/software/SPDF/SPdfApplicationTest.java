package stirling.software.SPDF;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ActiveProfiles;

import stirling.software.SPDF.model.ApplicationProperties;
import stirling.software.SPDF.config.security.TestSecurityConfiguration;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { SPdfApplication.class, TestSecurityConfiguration.class })
@ActiveProfiles("test")
public class SPdfApplicationTest {

    @Mock
    private Environment env;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private SPdfApplication sPdfApplication;

    private ConfigurableApplicationContext context;

    private final Path staticPath = Path.of("test/customFiles/static/");
    private final Path templatesPath = Path.of("test/customFiles/templates/");

    @BeforeEach
    public void setUp() throws IOException {
        sPdfApplication = new SPdfApplication();
        sPdfApplication.setServerPortStatic("8080");

        // Cleanup directories if they exist
        deleteDirectory(staticPath);
        deleteDirectory(templatesPath);

        // Ensure the directories are created for testing
        Files.createDirectories(staticPath);
        Files.createDirectories(templatesPath);
    }

    @Test
    public void testSetServerPortStatic() {
        sPdfApplication.setServerPortStatic("9090");
        assertEquals("9090", SPdfApplication.getStaticPort());
    }

    @Test
    public void testMainApplicationStartup() throws IOException, InterruptedException {

        // Run the main method in a separate thread to avoid blocking the test
        Thread mainThread = new Thread(() -> {
            try {
                context = SpringApplication.run(SPdfApplication.class);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Application failed to start", e);
            }
        });
        mainThread.start();
        mainThread.join();

        // Logging for debugging
        System.out.println("Checking if directories were created...");

        boolean staticPathExists = Files.exists(staticPath);
        boolean templatesPathExists = Files.exists(templatesPath);

        System.out.println("test/customFiles/static/ exists: " + staticPathExists);
        System.out.println("test/customFiles/templates/ exists: " + templatesPathExists);

        assertTrue(staticPathExists, "Directory test/customFiles/static/ does not exist.");
        assertTrue(templatesPathExists, "Directory test/customFiles/templates/ does not exist.");

        // Close the application context
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testGetStaticPort() {
        assertEquals("8080", SPdfApplication.getStaticPort());
    }

    @Test
    public void testGetNonStaticPort() {
        assertEquals("8080", sPdfApplication.getNonStaticPort());
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted((a, b) -> b.compareTo(a)) // reverse order to delete children first
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }
}
