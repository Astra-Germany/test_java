package stirling.software.SPDF.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ConfigInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(ConfigInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            ensureConfigExists();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize application configuration", e);
        }
    }

    public void ensureConfigExists() throws IOException, URISyntaxException {
        // Define the path to the external config directory
        Path destPath = Paths.get("configs", "settings.yml");

        // Check if the file already exists
        if (Files.notExists(destPath)) {
            // Ensure the destination directory exists
            Files.createDirectories(destPath.getParent());

            // Copy the resource from classpath to the external directory
            try (InputStream in =
                    getClass().getClassLoader().getResourceAsStream("settings.yml.template")) {
                if (in != null) {
                    Files.copy(in, destPath);
                } else {
                    throw new FileNotFoundException(
                            "Resource file not found: settings.yml.template");
                }
            }
        } else {
            writeNewSettings();
        }

        // Create custom settings file if it doesn't exist
        Path customSettingsPath = Paths.get("configs", "custom_settings.yml");
        if (!Files.exists(customSettingsPath)) {
            Files.createFile(customSettingsPath);
        }
    }

    private void writeNewSettings()
            throws InvalidConfigurationException, IOException, URISyntaxException {
        // Define the path to the config settings file
        Path settingsPath = Paths.get("configs", "settings.yml");
        // Load the template resource
        URL settingsTemplateResource =
                getClass().getClassLoader().getResource("settings.yml.template");
        if (settingsTemplateResource == null) {
            throw new IOException("Resource not found: settings.yml.template");
        }

        // Create a temporary file to copy the resource content
        Path tempTemplatePath = Files.createTempFile("settings.yml", ".template");
        try (InputStream in = settingsTemplateResource.openStream()) {
            Files.copy(in, tempTemplatePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // Initialize YamlFile objects for the template and main settings
        final YamlFile settingsYamlTemplate = new YamlFile(tempTemplatePath.toFile());
        final YamlFile settingsYaml = new YamlFile(settingsPath.toFile());
        final YamlFile tempSetting = new YamlFile(settingsPath.toFile());

        settingsYamlTemplate.loadWithComments();
        settingsYaml.loadWithComments();

        // Load headers and comments
        String header = settingsYamlTemplate.getHeader();

        // Create a new file for temporary settings
        tempSetting.createNewFile(true);
        tempSetting.setHeader(header);

        // Get all keys from the template
        List<String> keys =
                Arrays.asList(settingsYamlTemplate.getKeys(true).toArray(new String[0]));

        for (String key : keys) {
            if (!key.contains(".")) {
                // Add blank lines and comments to specific sections
                tempSetting.path(key).comment(settingsYamlTemplate.getComment(key)).blankLine();
                continue;
            }
            // Copy settings from the template to the settings file
            changeConfigItemFromCommentToKeyValue(
                    settingsYamlTemplate, settingsYaml, tempSetting, key);
        }

        // Save the temporary settings file
        tempSetting.save();
    }

    private void changeConfigItemFromCommentToKeyValue(
            final YamlFile settingsYamlTemplate,
            final YamlFile settingsYaml,
            final YamlFile tempSetting,
            String s) {
        if (settingsYaml.get(s) == null && settingsYamlTemplate.get(s) != null) {
            // If the key is only in the template, add it to the temporary settings with comments
            tempSetting
                    .path(s)
                    .set(settingsYamlTemplate.get(s))
                    .comment(settingsYamlTemplate.getComment(s, CommentType.BLOCK))
                    .commentSide(settingsYamlTemplate.getComment(s, CommentType.SIDE));
        } else if (settingsYaml.get(s) != null && settingsYamlTemplate.get(s) != null) {
            // If the key is in both, update the temporary settings with the main settings' value
            // and comments
            tempSetting
                    .path(s)
                    .set(settingsYaml.get(s))
                    .commentSide(settingsYamlTemplate.getComment(s, CommentType.SIDE));
        } else {
            // Log if the key is not found in both YAML files
            logger.info("Key not found in both YAML files: " + s);
        }
    }
}
