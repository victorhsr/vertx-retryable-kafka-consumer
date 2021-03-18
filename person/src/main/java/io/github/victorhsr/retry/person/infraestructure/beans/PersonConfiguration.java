package io.github.victorhsr.retry.person.infraestructure.beans;

import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.DefaultRegisterPersonUC;
import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.RegisterPersonUC;
import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.port.PublishPersonRegisteredEventPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Classe que prove instancias para que sejam
 * gerenciadas pelo spring-context
 * </p>
 *
 * <p>
 * TL;DR O core da aplicacao nao esta acoplado a nenhum
 * framework DI, ou banco de dados/file system/etc,
 * seguindo a logica de uma aplicacao hexagonal
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class PersonConfiguration {

    @Bean
    public RegisterPersonUC getRegisterPersonUC(final PublishPersonRegisteredEventPort publishPersonRegisteredEventPort) {
        return new DefaultRegisterPersonUC(publishPersonRegisteredEventPort);
    }

}
