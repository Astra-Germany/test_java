package stirling.software.SPDF.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

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
            private Boolean autoCreateUser = false;
            private String useAsUsername;
            private Collection<String> scopes = new ArrayList<>();
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
                return useAsUsername;
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

            protected boolean isValid(String value, String name) {
                if (value != null && !value.trim().isEmpty()) {
                    return true;
                }
                return false;
            }

            protected boolean isValid(Collection<String> value, String name) {
                if (value != null && !value.isEmpty()) {
                    return true;
                }
                return false;
            }

            public boolean isSettingsValid() {
                return isValid(this.getIssuer(), "issuer")
                        && isValid(this.getClientId(), "clientId")
                        && isValid(this.getClientSecret(), "clientSecret")
                        && isValid(this.getScopes(), "scopes")
                        && isValid(this.getUseAsUsername(), "useAsUsername");
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
                private GoogleProvider google = new GoogleProvider();
                private GithubProvider github = new GithubProvider();
                private KeycloakProvider keycloak = new KeycloakProvider();

                public Provider get(String registrationId) throws Exception {
                    switch (registrationId) {
                        case "gogole":
                            return getGoogle();
                        case "github":
                            return getGithub();
                        case "keycloak":
                            return getKeycloak();
                        default:
                            break;
                    }
                    throw new Exception("Provider not supported, use custom setting.");
                }

                public GoogleProvider getGoogle() {
                    return google;
                }

                public void setGoogle(GoogleProvider google) {
                    this.google = google;
                }

                public GithubProvider getGithub() {
                    return github;
                }

                public void setGithub(GithubProvider github) {
                    this.github = github;
                }

                public KeycloakProvider getKeycloak() {
                    return keycloak;
                }

                public void setKeycloak(KeycloakProvider keycloak) {
                    this.keycloak = keycloak;
                }

                @Override
                public String toString() {
                    return "Client [google="
                            + google
                            + ", github="
                            + github
                            + ", keycloak="
                            + keycloak
                            + "]";
                }
            }
        }
    }

    public static class GoogleProvider extends Provider {
        private String googleClientId;
        private String googleClientSecret;
        private Collection<String> googleScope = new ArrayList<>();
        private String googleuseasusername = "email";

        private static final String authorizationUri =
                "https://accounts.google.com/o/oauth2/v2/auth";
        private static final String tokenUri = "https://www.googleapis.com/oauth2/v4/token";
        private static final String userInfoUri =
                "https://www.googleapis.com/oauth2/v3/userinfo?alt=json";

        public String getAuthorizationuri() {
            return authorizationUri;
        }

        public String getTokenuri() {
            return tokenUri;
        }

        public String getUserinfouri() {
            return userInfoUri;
        }

        @Override
        public String getUseAsUsername() {
            return googleuseasusername;
        }

        public void setGoogleuseasusername(String googleuseasusername) {
            this.googleuseasusername = googleuseasusername;
        }

        @Override
        public Collection<String> getScope() {
            if (googleScope == null || googleScope.isEmpty()) {
                googleScope.add("https://www.googleapis.com/auth/userinfo.email");
                googleScope.add("https://www.googleapis.com/auth/userinfo.profile");
            }
            return googleScope;
        }

        public void setGooglescope(String googlescope) {
            this.googleScope =
                    Arrays.stream(googlescope.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        @Override
        public String getClientSecret() {
            return googleClientSecret;
        }

        public void setGoogleclientsecret(String googleclientsecret) {
            this.googleClientSecret = googleclientsecret;
        }

        @Override
        public String getClientId() {
            return googleClientId;
        }

        public void setGoogleclientid(String googleclientid) {
            this.googleClientId = googleclientid;
        }

        @Override
        public String toString() {
            return "Google [clientId="
                    + googleClientId
                    + ", clientSecret="
                    + (googleClientSecret != null && !googleClientSecret.isEmpty()
                            ? "MASKED"
                            : "NULL")
                    + ", scope="
                    + googleScope
                    + ", useAsUsername="
                    + googleuseasusername
                    + "]";
        }

        @Override
        public String getName() {
            return "google";
        }

        @Override
        public boolean isSettingsValid() {
            return super.isValid(this.getClientId(), "clientId")
                    && super.isValid(this.getClientSecret(), "clientSecret")
                    && super.isValid(this.getScope(), "scope")
                    && isValid(this.getUseAsUsername(), "useAsUsername");
        }
    }

    public static class GithubProvider extends Provider {
        private String githubclientid;
        private String githubclientsecret;
        private Collection<String> githubScope = new ArrayList<>();
        private String githubuseasusername = "login";

        private static final String authorizationUri = "https://github.com/login/oauth/authorize";
        private static final String tokenUri = "https://github.com/login/oauth/access_token";
        private static final String userInfoUri = "https://api.github.com/user";

        public String getAuthorizationuri() {
            return authorizationUri;
        }

        public String getTokenuri() {
            return tokenUri;
        }

        public String getUserinfouri() {
            return userInfoUri;
        }

        @Override
        public String getUseAsUsername() {
            return githubuseasusername;
        }

        public void setGithubuseasusername(String githubuseasusername) {
            this.githubuseasusername = githubuseasusername;
        }

        @Override
        public Collection<String> getScope() {
            if (githubScope == null || githubScope.isEmpty()) {
                githubScope.add("read:user");
            }
            return githubScope;
        }

        public void setGithubscope(String githubscope) {
            this.githubScope =
                    Arrays.stream(githubscope.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        @Override
        public String getClientSecret() {
            return githubclientsecret;
        }

        public void setGithubclientsecret(String githubclientsecret) {
            this.githubclientsecret = githubclientsecret;
        }

        @Override
        public String getClientId() {
            return githubclientid;
        }

        public void setGithubclientid(String githubclientid) {
            this.githubclientid = githubclientid;
        }

        @Override
        public String toString() {
            return "GitHub [clientId="
                    + githubclientid
                    + ", clientSecret="
                    + (githubclientsecret != null && !githubclientsecret.isEmpty()
                            ? "MASKED"
                            : "NULL")
                    + ", scope="
                    + githubScope
                    + ", useAsUsername="
                    + githubuseasusername
                    + "]";
        }

        @Override
        public String getName() {
            return "github";
        }

        @Override
        public boolean isSettingsValid() {
            return super.isValid(this.getClientId(), "clientId")
                    && super.isValid(this.getClientSecret(), "clientSecret")
                    && super.isValid(this.getScope(), "scope")
                    && isValid(this.getUseAsUsername(), "useAsUsername");
        }
    }

    public static class KeycloakProvider extends Provider {
        private String keycloakIssuer;
        private String keycloakClientId;
        private String keycloakClientSecret;
        private Collection<String> keycloakScope = new ArrayList<>();
        private String keycloakUseAsUsername = "email";

        @Override
        public String getUseAsUsername() {
            return keycloakUseAsUsername;
        }

        public void setKeycloakuseasusername(String keycloakuseasusername) {
            this.keycloakUseAsUsername = keycloakuseasusername;
        }

        @Override
        public Collection<String> getScope() {
            if (keycloakScope == null || keycloakScope.isEmpty()) {
                keycloakScope.add("openid");
                keycloakScope.add("profile");
                keycloakScope.add("email");
            }
            return keycloakScope;
        }

        public void setKeycloakscope(String keycloakscope) {
            this.keycloakScope =
                    Arrays.stream(keycloakscope.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        @Override
        public String getClientSecret() {
            return keycloakClientSecret;
        }

        public void setKeycloakclientsecret(String keycloakclientsecret) {
            this.keycloakClientSecret = keycloakclientsecret;
        }

        @Override
        public String getClientId() {
            return keycloakClientId;
        }

        public void setKeycloakclientid(String keycloakclientid) {
            this.keycloakClientId = keycloakclientid;
        }

        @Override
        public String getIssuer() {
            return keycloakIssuer;
        }

        public void setKeycloakIssuer(String keycloakissuer) {
            this.keycloakIssuer = keycloakissuer;
        }

        @Override
        public String toString() {
            return "Keycloak [issuer="
                    + keycloakIssuer
                    + ", clientId="
                    + keycloakClientId
                    + ", clientSecret="
                    + (keycloakClientSecret != null && !keycloakClientSecret.isEmpty()
                            ? "MASKED"
                            : "NULL")
                    + ", scope="
                    + keycloakScope
                    + ", useAsUsername="
                    + keycloakUseAsUsername
                    + "]";
        }

        @Override
        public String getName() {
            return "keycloak";
        }

        @Override
        public boolean isSettingsValid() {
            return super.isSettingsValid();
        }
    }

    public static class System {
        private String defaultLocale;
        private Boolean googlevisibility;
        private Boolean enableAlphaFunctionality;
        private Boolean showUpdate;
        private Boolean showUpdateOnlyAdmin;
        private Boolean customHTMLFiles;

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

        public Boolean getEnableAlphaFunctionality() {
            return enableAlphaFunctionality;
        }

        public void setEnableAlphaFunctionality(Boolean enableAlphaFunctionality) {
            this.enableAlphaFunctionality = enableAlphaFunctionality;
        }

        public Boolean getShowUpdate() {
            return showUpdate;
        }

        public void setShowUpdate(Boolean showUpdate) {
            this.showUpdate = showUpdate;
        }

        public Boolean getShowUpdateOnlyAdmin() {
            return showUpdateOnlyAdmin;
        }

        public void setShowUpdateOnlyAdmin(Boolean showUpdateOnlyAdmin) {
            this.showUpdateOnlyAdmin = showUpdateOnlyAdmin;
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
