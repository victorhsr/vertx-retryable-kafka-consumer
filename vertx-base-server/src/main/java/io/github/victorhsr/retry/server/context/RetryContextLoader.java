package io.github.victorhsr.retry.server.context;

import io.github.victorhsr.retry.server.profile.ProfileSetUp;
import io.reactivex.rxjava3.core.Completable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class RetryContextLoader {

    private static final String SERVER_BASE_PACKAGE = "io.github.victorhsr.retry.server";
    private static final ProfileSetUp PROFILE_SET_UP = new ProfileSetUp();

    private static AnnotationConfigApplicationContext CONTEXT;

    public static Completable loadContext(final String... basePackages) {
        return Completable.create(emitter -> {
            if (Objects.nonNull(CONTEXT))
                emitter.onComplete();

            PROFILE_SET_UP.setUp();
            final List<String> basePackagesToUse = new ArrayList<>(Arrays.asList(basePackages));
            basePackagesToUse.add(SERVER_BASE_PACKAGE);

            CONTEXT = new AnnotationConfigApplicationContext(basePackagesToUse.toArray(new String[0]));
            emitter.onComplete();
        });
    }

    public static <T> T getBean(final Class<T> beanType) {
        if (Objects.isNull(CONTEXT))
            throw new RuntimeException("The CONTEXT is not loaded");

        return CONTEXT.getBean(beanType);
    }

    public static void clearContext() {
        CONTEXT = null;
    }

}
