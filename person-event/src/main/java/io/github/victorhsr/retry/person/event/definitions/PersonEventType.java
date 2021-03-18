package io.github.victorhsr.retry.person.event.definitions;

/**
 * Informacoes sobre os tipos de evento que ocorrem
 * em person aggregate
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public enum PersonEventType implements TypeWrapper {

    PERSON_REGISTERED {
        @Override
        public String getType() {
            return "PERSON_REGISTERED";
        }
    };

}
