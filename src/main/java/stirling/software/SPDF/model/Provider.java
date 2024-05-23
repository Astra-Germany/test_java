package stirling.software.SPDF.model;

import java.util.Collection;

public class Provider {
    private String name;
    private String clientId;
    private String clientSecret;
    private String issuer;
    private String useAsUsername;
    private Collection<String> scope;

    public Collection<String> getScope() {
        return scope;
    }

    public String getUseAsUsername() {
        return useAsUsername;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    protected boolean isValid(String value, String name) {
        if (value != null && !value.trim().isEmpty()) {
            return true;
        }
        return false;
        // throw new IllegalArgumentException(getName() + ": " + name + " is required!");
    }

    protected boolean isValid(Collection<String> value, String name) {
        if (value != null && !value.isEmpty()) {
            return true;
        }
        return false;
        // throw new IllegalArgumentException(getName() + ": " + name + " is required!");
    }

    public boolean isSettingsValid() {
        return isValid(this.getIssuer(), "issuer")
                && isValid(this.getClientId(), "clientId")
                && isValid(this.getClientSecret(), "clientSecret")
                && isValid(this.getScope(), "scope")
                && isValid(this.getUseAsUsername(), "useAsUsername");
    }
}
