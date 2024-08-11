package stirling.software.SPDF.plugin;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigInitializer implements CommandLineRunner {

    private static final String CONFIG_DIR = "config";
    private static final String PLUGIN_DIR = "config/plugins";

    @Override
    public void run(String... args) throws Exception {
        createDirIfNotExists(CONFIG_DIR);
        createDirIfNotExists(PLUGIN_DIR);

        // Hier kannst du auch die settings.yml und db-Datei erstellen oder laden
    }

    private void createDirIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
