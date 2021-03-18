package io.github.victorhsr.retry.server.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.StringUtils;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class ProfileSetUp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileSetUp.class);

    public static final String PROFILE_ENV = "profile";
    public static final String DEFAULT_PROFILE = Profile.DEVELOPMENT;

    public String setUp() {
        final String activeProfile = this.getActiveProfile();
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
        LOGGER.info("Environment preparado para o profile ativo '{}'", activeProfile);
        return activeProfile;
    }

    private String getActiveProfile() {
        LOGGER.info("Buscando profile ativo a partir da variavel de ambiente '{}'...", PROFILE_ENV);
        String activeProfile = System.getenv(PROFILE_ENV);

        if (StringUtils.isEmpty(activeProfile)) {
            LOGGER.info("Nenhum profile ativo encontrado, setando paro o profile padrao '{}'", DEFAULT_PROFILE);
            activeProfile = DEFAULT_PROFILE;
        } else {
            LOGGER.info("Profile encontrado, '{}'", activeProfile);
        }
        return activeProfile;
    }
}
