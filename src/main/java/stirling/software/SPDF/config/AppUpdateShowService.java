package stirling.software.SPDF.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import stirling.software.SPDF.model.ApplicationProperties;
import stirling.software.SPDF.model.User;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userRepository == null || authentication == null || !authentication.isAuthenticated()) {
            return showUpdate && !showUpdateOnlyAdmin;
        }

        if (authentication.getName().equalsIgnoreCase("anonymousUser")) {
            return showUpdate && !showUpdateOnlyAdmin;
        }

        if (userRepository != null) {
            Optional<?> user = findUserByUsername(authentication.getName());
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
}
