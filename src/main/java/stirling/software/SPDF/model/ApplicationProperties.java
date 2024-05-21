package stirling.software.SPDF.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import stirling.software.SPDF.config.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties(prefix = "")
@PropertySource(value = "file:./configs/settings.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationProperties {
    private Security security;
    private System system;
    private Ui ui;
    private Endpoints endpoints;
    private Metrics metrics;
    private AutomaticallyGenerated automaticallyGenerated;
    private AutoPipeline autoPipeline;

    public AutoPipeline getAutoPipeline() {
        return autoPipeline != null ? autoPipeline : new AutoPipeline();
    }

    public void setAutoPipeline(AutoPipeline autoPipeline) {
        this.autoPipeline = autoPipeline;
    }

    public Security getSecurity() {
        return security != null ? security : new Security();
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public System getSystem() {
        return system != null ? system : new System();
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public Ui getUi() {
        return ui != null ? ui : new Ui();
    }

    public void setUi(Ui ui) {
        this.ui = ui;
    }

    public Endpoints getEndpoints() {
        return endpoints != null ? endpoints : new Endpoints();
    }

    public void setEndpoints(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    public Metrics getMetrics() {
        return metrics != null ? metrics : new Metrics();
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public AutomaticallyGenerated getAutomaticallyGenerated() {
        return automaticallyGenerated != null
                ? automaticallyGenerated
                : new AutomaticallyGenerated();
    }

    public void setAutomaticallyGenerated(AutomaticallyGenerated automaticallyGenerated) {
        this.automaticallyGenerated = automaticallyGenerated;
    }

    @Override
    public String toString() {
        return "ApplicationProperties [security="
                + security
                + ", system="
                + system
                + ", ui="
                + ui
                + ", endpoints="
                + endpoints
                + ", metrics="
                + metrics
                + ", automaticallyGenerated="
                + automaticallyGenerated
                + ", autoPipeline="
                + autoPipeline
                + "]";
    }

    public static class AutoPipeline {
        private String outputFolder;

        public String getOutputFolder() {
            return outputFolder;
        }

        public void setOutputFolder(String outputFolder) {
            this.outputFolder = outputFolder;
        }

        @Override
        public String toString() {
            return "AutoPipeline [outputFolder=" + outputFolder + "]";
        }
    }

    public static class Security {
        private Boolean enableLogin;
        private Boolean csrfDisabled;
        private InitialLogin initialLogin;
        private OAUTH2 oauth2;
        private int loginAttemptCount;
        private long loginResetTimeMinutes;

        public int getLoginAttemptCount() {
            return loginAttemptCount;
        }

        public void setLoginAttemptCount(int loginAttemptCount) {
            this.loginAttemptCount = loginAttemptCount;
        }

        public long getLoginResetTimeMinutes() {
            return loginResetTimeMinutes;
        }

        public void setLoginResetTimeMinutes(long loginResetTimeMinutes) {
            this.loginResetTimeMinutes = loginResetTimeMinutes;
        }

        public InitialLogin getInitialLogin() {
            return initialLogin != null ? initialLogin : new InitialLogin();
        }

        public void setInitialLogin(InitialLogin initialLogin) {
            this.initialLogin = initialLogin;
        }

        public OAUTH2 getOAUTH2() {
            return oauth2 != null ? oauth2 : new OAUTH2();
        }

        public void setOAUTH2(OAUTH2 oauth2) {
            this.oauth2 = oauth2;
        }

        public Boolean getEnableLogin() {
            return enableLogin;
        }

        public void setEnableLogin(Boolean enableLogin) {
            this.enableLogin = enableLogin;
        }

        public Boolean getCsrfDisabled() {
            return csrfDisabled;
        }

        public void setCsrfDisabled(Boolean csrfDisabled) {
            this.csrfDisabled = csrfDisabled;
        }

        @Override
        public String toString() {
            return "Security [enableLogin="
                    + enableLogin
                    + ", oauth2="
                    + oauth2
                    + ", initialLogin="
                    + initialLogin
                    + ", csrfDisabled="
                    + csrfDisabled
                    + "]";
        }

        public static class InitialLogin {
            private String username;
            private String password;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            @Override
            public String toString() {
                return "InitialLogin [username="
                        + username
                        + ", password="
                        + (password != null && !password.isEmpty() ? "MASKED" : "NULL")
                        + "]";
            }
        }

        public static class OAUTH2 {
            private Boolean enabled = false;
            private String issuer;
            private String clientId;
            private String clientSecret;
            private Boolean autoCreateUser = true; // REMOVE before deploy
            private String useAsUsername;
            private Collection<String> scopes = new ArrayList<String>();
            private String provider;
            private Client client = new Client();

            public Boolean getEnabled() {
                return enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }

            public String getIssuer() {
                return issuer;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }

            public Boolean getAutoCreateUser() {
                return autoCreateUser;
            }

            public void setAutoCreateUser(Boolean autoCreateUser) {
                this.autoCreateUser = autoCreateUser;
            }

            public String getUseAsUsername() {
                return useAsUsername != null && !useAsUsername.trim().isEmpty()
                        ? useAsUsername
                        : "email";
            }

            public void setUseAsUsername(String useAsUsername) {
                this.useAsUsername = useAsUsername;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public Collection<String> getScopes() {
                return scopes;
            }

            public void setScopes(String scope) {
                List<String> scopesList =
                        Arrays.stream(scope.split(","))
                                .map(String::trim)
                                .collect(Collectors.toList());
                this.scopes.addAll(scopesList);
            }

            public Client getClient() {
                return client;
            }

            public void setClient(Client client) {
                this.client = client;
            }

            @Override
            public String toString() {
                return "OAUTH2 [enabled="
                        + enabled
                        + ", issuer="
                        + issuer
                        + ", clientId="
                        + clientId
                        + ", clientSecret="
                        + (clientSecret != null && !clientSecret.isEmpty() ? "MASKED" : "NULL")
                        + ", autoCreateUser="
                        + autoCreateUser
                        + ", useAsUsername="
                        + useAsUsername
                        + ", provider="
                        + provider
                        + ", scopes="
                        + scopes
                        + "]";
            }

            public static class Client {
                private Providers providers = new Providers();

                public Providers getProviders() {
                    return providers;
                }

                public void setProviders(Providers providers) {
                    this.providers = providers;
                }

                @Override
                public String toString() {
                    return "Client [providers=" + providers + "]";
                }

                public static class Providers {
                    private Provider google = new Provider();
                    private Provider github = new Provider();
                    private Provider keycloak = new Provider();

                    public Provider getGoogle() {
                        return google;
                    }

                    public void setGoogle(Provider google) {
                        this.google = google;
                    }

                    public Provider getGithub() {
                        return github;
                    }

                    public void setGithub(Provider github) {
                        this.github = github;
                    }

                    public Provider getKeycloak() {
                        return keycloak;
                    }

                    public void setKeycloak(Provider keycloak) {
                        this.keycloak = keycloak;
                    }

                    @Override
                    public String toString() {
                        return "Providers [google="
                                + google
                                + ", github="
                                + github
                                + ", keycloak="
                                + keycloak
                                + "]";
                    }

                    public Provider get(String registrationId) {
                        switch (registrationId) {
                            case "keycloak":
                                return this.keycloak;
                            case "google":
                                return this.google;
                            case "github":
                                return this.github;
                            default:
                                return new Provider();
                        }
                    }
                }

                public static class Provider {
                    private String issuer;
                    private String clientId;
                    private String clientSecret;
                    private Collection<String> scopes = new ArrayList<String>();
                    private String useAsUsername;

                    public String getIssuer() {
                        return issuer;
                    }

                    public void setIssuer(String issuer) {
                        this.issuer = issuer;
                    }

                    public String getClientId() {
                        return clientId;
                    }

                    public void setClientId(String clientId) {
                        this.clientId = clientId;
                    }

                    public String getClientSecret() {
                        return clientSecret;
                    }

                    public void setClientSecret(String clientSecret) {
                        this.clientSecret = clientSecret;
                    }

                    public Collection<String> getScopes() {
                        return scopes;
                    }

                    public void setScopes(String scope) {
                        List<String> scopesList =
                                Arrays.stream(scope.split(","))
                                        .map(String::trim)
                                        .collect(Collectors.toList());
                        this.scopes.addAll(scopesList);
                    }

                    public String getUseAsUsername() {
                        return useAsUsername;
                    }

                    public void setUseAsUsername(String useAsUsername) {
                        this.useAsUsername = useAsUsername;
                    }

                    @Override
                    public String toString() {
                        return "Provider [issuer="
                                + issuer
                                + "clientId="
                                + clientId
                                + ", clientSecret="
                                + (clientSecret != null && !clientSecret.isEmpty()
                                        ? "MASKED"
                                        : "NULL")
                                + ", scopes="
                                + scopes
                                + ", useAsUsername="
                                + useAsUsername
                                + "]";
                    }
                }
            }
        }
    }

    public static class System {
        private String defaultLocale;
        private Boolean googlevisibility;
        private Boolean showUpdate;
        private Boolean showUpdateOnlyAdmin;
        private Boolean customHTMLFiles;

        public boolean isCustomHTMLFiles() {
            return customHTMLFiles;
        }

        public void setCustomHTMLFiles(boolean customHTMLFiles) {
            this.customHTMLFiles = customHTMLFiles;
        }

        public boolean getShowUpdateOnlyAdmin() {
            return showUpdateOnlyAdmin;
        }

        public void setShowUpdateOnlyAdmin(boolean showUpdateOnlyAdmin) {
            this.showUpdateOnlyAdmin = showUpdateOnlyAdmin;
        }

        public boolean getShowUpdate() {
            return showUpdate;
        }

        public void setShowUpdate(boolean showUpdate) {
            this.showUpdate = showUpdate;
        }

        private Boolean enableAlphaFunctionality;

        public Boolean getEnableAlphaFunctionality() {
            return enableAlphaFunctionality;
        }

        public void setEnableAlphaFunctionality(Boolean enableAlphaFunctionality) {
            this.enableAlphaFunctionality = enableAlphaFunctionality;
        }

        public String getDefaultLocale() {
            return defaultLocale;
        }

        public void setDefaultLocale(String defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        public Boolean getGooglevisibility() {
            return googlevisibility;
        }

        public void setGooglevisibility(Boolean googlevisibility) {
            this.googlevisibility = googlevisibility;
        }

        @Override
        public String toString() {
            return "System [defaultLocale="
                    + defaultLocale
                    + ", googlevisibility="
                    + googlevisibility
                    + ", enableAlphaFunctionality="
                    + enableAlphaFunctionality
                    + ", showUpdate="
                    + showUpdate
                    + ", showUpdateOnlyAdmin="
                    + showUpdateOnlyAdmin
                    + "]";
        }
    }

    public static class Ui {
        private String appName;
        private String homeDescription;
        private String appNameNavbar;

        public String getAppName() {
            return appName != null && !appName.trim().isEmpty() ? appName : null;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getHomeDescription() {
            return homeDescription != null && !homeDescription.trim().isEmpty()
                    ? homeDescription
                    : null;
        }

        public void setHomeDescription(String homeDescription) {
            this.homeDescription = homeDescription;
        }

        public String getAppNameNavbar() {
            return appNameNavbar != null && !appNameNavbar.trim().isEmpty() ? appNameNavbar : null;
        }

        public void setAppNameNavbar(String appNameNavbar) {
            this.appNameNavbar = appNameNavbar;
        }

        @Override
        public String toString() {
            return "UserInterface [appName="
                    + appName
                    + ", homeDescription="
                    + homeDescription
                    + ", appNameNavbar="
                    + appNameNavbar
                    + "]";
        }
    }

    public static class Endpoints {
        private List<String> toRemove;
        private List<String> groupsToRemove;

        public List<String> getToRemove() {
            return toRemove;
        }

        public void setToRemove(List<String> toRemove) {
            this.toRemove = toRemove;
        }

        public List<String> getGroupsToRemove() {
            return groupsToRemove;
        }

        public void setGroupsToRemove(List<String> groupsToRemove) {
            this.groupsToRemove = groupsToRemove;
        }

        @Override
        public String toString() {
            return "Endpoints [toRemove=" + toRemove + ", groupsToRemove=" + groupsToRemove + "]";
        }
    }

    public static class Metrics {
        private Boolean enabled;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return "Metrics [enabled=" + enabled + "]";
        }
    }

    public static class AutomaticallyGenerated {
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "AutomaticallyGenerated [key="
                    + (key != null && !key.isEmpty() ? "MASKED" : "NULL")
                    + "]";
        }
    }
}
