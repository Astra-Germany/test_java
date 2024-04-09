package stirling.software.SPDF.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.RunScript;

public class H2Migration {

    private static void exportDatabase(String url, String user, String password, String scriptFile)
            throws SQLException, FileNotFoundException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Script.process(conn, scriptFile, "", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void importDatabase(String url, String user, String password, String scriptFile)
            throws SQLException, FileNotFoundException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            RunScript.execute(conn, new FileReader(scriptFile));
        }
    }

    public static void migrateDatabase(
            String oldUrl, String user, String password, String newUrl, String scriptFile)
            throws Exception {
        // Exportieren der alten Datenbank
        // exportDatabase(oldUrl, user, password, scriptFile);
        // Importieren in die neue Datenbank
        importDatabase(newUrl, user, password, scriptFile);
    }

    public static void main(String[] args) {
        try {
            String projectPath = System.getProperty("user.dir");

            // Ausgeben des Pfads
            System.out.println("Aktuelles Projektverzeichnis: " + projectPath);
            Path path = Paths.get("configs", "stirling-pdf-DB");
            System.out.println(path.toFile().toString());
            String oldUrl = "jdbc:h2:" + path.toAbsolutePath().toString(); // The old database URL
            String newUrl =
                    "jdbc:h2:"
                            + Paths.get("configs", "stirling-pdf-DB-mi-new")
                                    .toAbsolutePath()
                                    .toString(); // The new database URL
            String user = "sa"; // Your DB user
            String password = ""; // Your DB password
            String scriptFile = "configs/script.sql";

            migrateDatabase(oldUrl, user, password, newUrl, scriptFile);
            System.out.println("Migration completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
