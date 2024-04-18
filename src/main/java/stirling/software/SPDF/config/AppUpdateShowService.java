package stirling.software.SPDF.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import stirling.software.SPDF.model.ApplicationProperties;
// import stirling.software.SPDF.model.User;
// import stirling.software.SPDF.repository.UserRepository;

@Configuration
public class AppUpdateShowService {

    @Bean
    public AppUpdateService showUpdate(
            ApplicationProperties applicationProperties) {
        return new AppUpdateService(applicationProperties);
    }
}

class AppUpdateService {

    private Object userRepository;
    private ApplicationProperties applicationProperties;

    public AppUpdateService(ApplicationProperties applicationProperties) {
        this.userRepository = getUserRepositoryInstance();
        this.applicationProperties = applicationProperties;
    }

    private Object getUserRepositoryInstance() {
        try {
            Class<?> repoClass = Class.forName("stirling.software.SPDF.repository.UserRepository");
            return repoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null; // Klasse nicht verfügbar oder Instanziierung fehlgeschlagen
        }
    }

    public boolean isShow() {
        boolean showUpdate = applicationProperties.getSystem().getShowUpdate();
        boolean showUpdateOnlyAdmin = applicationProperties.getSystem().getShowUpdateOnlyAdmin();
        Object authentication = getAuthentication();

        if (userRepository == null || authentication == null || !authentication.isAuthenticated()) {
            return showUpdate && !showUpdateOnlyAdmin;
        }

        String username = getName(authentication);
        if ("anonymousUser".equalsIgnoreCase(username)) {
            return showUpdate && !showUpdateOnlyAdmin;
        }

        if (userRepository != null) {
            Optional<?> user = findUserByUsername(username);
            if (user.isPresent() && showUpdateOnlyAdmin) {
                return "ROLE_ADMIN".equals(getRolesAsString(user.get())) && showUpdate;
            }
        }

        return showUpdate;
    }

    private Optional<?> findUserByUsername(String username) {
        try {
            return (Optional<?>) userRepository.getClass().getMethod("findByUsername", String.class).invoke(userRepository, username);
        } catch (Exception e) {
            return Optional.empty(); // Methode nicht gefunden oder Aufruf fehlgeschlagen
        }
    }

    private String getRolesAsString(Object user) {
        try {
            return (String) user.getClass().getMethod("getRolesAsString").invoke(user);
        } catch (Exception e) {
            return ""; // Methode nicht gefunden oder Aufruf fehlgeschlagen
        }
    }

    private Object getAuthentication() {
        try {
            Class<?> contextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            return contextHolderClass.getMethod("getContext").invoke(null).getClass().getMethod("getAuthentication").invoke(contextHolderClass.getMethod("getContext").invoke(null));
        } catch (Exception e) {
            return null; // Klasse oder Methode nicht gefunden
        }
    }

    private boolean isAuthenticated(Object authentication) {
        try {
            return (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
        } catch (Exception e) {
            return false; // Methode nicht gefunden oder Aufruf fehlgeschlagen
        }
    }

    private String getName(Object authentication) {
        try {
            return (String) authentication.getClass().getMethod("getName").invoke(authentication);
        } catch (Exception e) {
            return "anonymousUser"; // Methode nicht gefunden oder Aufruf fehlgeschlagen
        }
    }
}
