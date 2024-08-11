package stirling.software.SPDF.plugin;

public abstract class AbstractPlugin implements PluginInterface {
    protected String configPath;

    public AbstractPlugin(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void initialize() {
        // Gemeinsame Initialisierungslogik
    }

    @Override
    public abstract void execute();
}
