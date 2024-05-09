package stirling.software.SPDF.config.security.oauth2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stirling.software.SPDF.config.security.UserService;
import stirling.software.SPDF.model.ApplicationProperties;
import stirling.software.SPDF.model.ApplicationProperties.Security.OAUTH2;
import stirling.software.SPDF.model.AuthenticationType;

public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    ApplicationProperties applicationProperties;
    UserService userService;

    private static final Logger logger =
            LoggerFactory.getLogger(CustomOAuth2AuthenticationSuccessHandler.class);

    public CustomOAuth2AuthenticationSuccessHandler(
            ApplicationProperties applicationProperties, UserService userService) {
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        logger.info("onAuthenticationSuccess oAuth2 " + request.getAuthType());
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        logger.info("PETER: " + oauthUser.getName());
        OAUTH2 oAuth = applicationProperties.getSecurity().getOAUTH2();
        String username = oauthUser.getAttribute(oAuth.getUseAsUsername());
        if (userService.usernameExistsIgnoreCase(username)
                && userService.hasPassword(username)
                && !userService.isAuthenticationTypeByUsername(username, AuthenticationType.OAUTH2)
                && oAuth.getAutoCreateUser()) {
            response.sendRedirect(
                    request.getContextPath() + "/logout?oauth2AuthenticationError=true");
        } else {
            userService.processOAuth2PostLogin(username, oAuth.getAutoCreateUser());
            response.sendRedirect("/");
        }
    }
}
