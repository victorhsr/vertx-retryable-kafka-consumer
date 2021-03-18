package io.github.victorhsr.retry.server.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
@PropertySource("application-prod.properties")
@Profile(io.github.victorhsr.retry.server.profile.Profile.PRODUCTION)
public class ProdPropertiesSourceConfig {
}
