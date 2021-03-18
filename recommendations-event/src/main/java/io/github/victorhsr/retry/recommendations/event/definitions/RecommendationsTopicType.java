package io.github.victorhsr.retry.recommendations.event.definitions;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public enum RecommendationsTopicType implements TypeWrapper {

    RECOMMENDATIONS_GENERATED_TOPIC {
        @Override
        public String getType() {
            return "recommendations-generated-topic";
        }
    }

}
