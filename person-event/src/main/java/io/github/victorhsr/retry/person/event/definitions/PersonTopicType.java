package io.github.victorhsr.retry.person.event.definitions;

/**
 * Informacoes sobre os topicos disponiveis em person aggregate e seus
 * nomes
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public enum PersonTopicType implements TypeWrapper {

    PERSON_REGISTERED_TOPIC {
        @Override
        public String getType() {
            return "person-registered-topic";
        }
    }

}
