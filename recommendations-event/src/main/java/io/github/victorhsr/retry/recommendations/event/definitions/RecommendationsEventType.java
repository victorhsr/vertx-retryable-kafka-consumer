package io.github.victorhsr.retry.recommendations.event.definitions;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public enum RecommendationsEventType implements TypeWrapper {

    RECOMMENDATIONS_GENERATED {
        @Override
        public String getType() {
            return "RECOMMENDATIONS_GENERATED";
        }
    }

}
