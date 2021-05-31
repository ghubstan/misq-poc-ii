package org.misq.common.di.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation on type to indicate to the framework to inject the proper singleton implementation.
 * Modified from https://github.com/zeldan/your-own-dependency-injection-framework.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface Singleton {
}