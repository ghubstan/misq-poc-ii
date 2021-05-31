package org.misq.common.di;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.misq.common.di.mock.SimpleSingleton;
import org.misq.common.di.module.AbstractDependencyInjectionModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingletonInjectionTest {

    @Test
    @Order(1)
    public void testDuplicateSingletonInjectionShouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, () ->
                DependencyInjectorConfig.getInjector(new DuplicateSingletonModule()));
        assertEquals("Attempt to cache singleton object already present in singleton object map.", exception.getMessage());
    }

    @Test
    @Order(2)
    public void testSingletonInjection() {
        DependencyInjector dif = DependencyInjectorConfig.getInjector(new SingletonModule());
        var singleton1 = dif.getModule().getSingletonMapping(SimpleSingleton.class);
        var singleton2 = dif.getModule().getSingletonMapping(SimpleSingleton.class);
        assertEquals(singleton1, singleton2);
    }

    private class SingletonModule extends AbstractDependencyInjectionModule {
        @Override
        public void configure() {
            // createSingletonMapping(SimpleSingleton.class, new SimpleSingleton());
            createMapping(SimpleSingleton.class, new SimpleSingleton());
        }
    }

    private class DuplicateSingletonModule extends AbstractDependencyInjectionModule {
        @Override
        public void configure() {
            // createSingletonMapping(SimpleSingleton.class, new SimpleSingleton());
            // createSingletonMapping(SimpleSingleton.class, new SimpleSingleton());
            createMapping(SimpleSingleton.class, new SimpleSingleton());
            createMapping(SimpleSingleton.class, new SimpleSingleton());
        }
    }
}
