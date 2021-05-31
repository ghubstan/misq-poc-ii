package org.misq.common.di;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.misq.common.di.mock.SimpleCache;
import org.misq.common.di.mock.SimpleNode;
import org.misq.common.di.module.AbstractDependencyInjectionModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FieldInjectionTest {

    private static SimpleCache simpleCache = new SimpleCache();

    @Test
    @Order(1)
    public void testDuplicateFieldInjectionShouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, () ->
                DependencyInjectorConfig.getInjector(new MisConfiguredNodeAModuleConfig()));
        assertEquals("Attempt to cache key/value already present in object map.", exception.getMessage());
    }

    @Test
    @Order(2)
    public void testSingletonInjection() {
        DependencyInjector diNodeA = DependencyInjectorConfig.getInjector(new SimpleNodeAModuleConfig());
        var singleton1 = diNodeA.getModule().getSingletonMapping(SimpleCache.class);
        var singleton2 = diNodeA.getModule().getSingletonMapping(SimpleCache.class);
        assertEquals(singleton1, singleton2);

        DependencyInjector diNodeB = DependencyInjectorConfig.getInjector(new SimpleNodeBModuleConfig());
        var singleton3 = diNodeB.getModule().getSingletonMapping(SimpleCache.class);
        var singleton4 = diNodeB.getModule().getSingletonMapping(SimpleCache.class);
        assertEquals(singleton3, singleton4);

        assertEquals(singleton1, singleton3);
        assertEquals(singleton1, singleton4);
    }

    @Test
    @Order(3)
    public void testFieldInjection() {
        // Configure Node A
        DependencyInjector diNodeA = DependencyInjectorConfig.getInjector(new SimpleNodeAModuleConfig());
        SimpleNode nodeA = (SimpleNode) diNodeA.inject(SimpleNode.class);  // injectFieldsIntoClass
        assertEquals("Node-A", nodeA.getNodeName());
        assertEquals(50_000, nodeA.getPort());
        assertEquals(100, nodeA.getNumRequests());
        assertEquals(simpleCache, nodeA.getSimpleCache());


        // Configure Node B
        DependencyInjector diNodeB = DependencyInjectorConfig.getInjector(new SimpleNodeBModuleConfig());
        SimpleNode nodeB = (SimpleNode) diNodeB.inject(SimpleNode.class);  // injectFieldsIntoClass
        assertEquals("Node-B", nodeB.getNodeName());
        assertEquals(50_001, nodeB.getPort());
        assertEquals(101, nodeB.getNumRequests());
        assertEquals(simpleCache, nodeB.getSimpleCache());
    }

    private class MisConfiguredNodeAModuleConfig extends AbstractDependencyInjectionModule {
        @Override
        public void configure() {
            // Configure Node A, but don't create it here.
            createMapping("nodeName", "Node-A");
            createMapping("nodeName", "Node-A-Again");
            createMapping("port", 50_000);
            createMapping("numRequests", 100);
            createMapping(SimpleCache.class, simpleCache);
            //createSingletonMapping(SimpleCache.class, simpleCache);
        }
    }

    private class SimpleNodeAModuleConfig extends AbstractDependencyInjectionModule {
        @Override
        public void configure() {
            // Configure Node B, but don't create it here.
            createMapping("nodeName", "Node-A");
            createMapping("port", 50_000);
            createMapping("numRequests", 100);
            createMapping(SimpleCache.class, simpleCache);
            //createSingletonMapping(SimpleCache.class, simpleCache);
        }
    }

    private class SimpleNodeBModuleConfig extends AbstractDependencyInjectionModule {
        @Override
        public void configure() {
            createMapping("nodeName", "Node-B");
            createMapping("port", 50_001);
            createMapping("numRequests", 101);
            createMapping(SimpleCache.class, simpleCache);
            //createSingletonMapping(SimpleCache.class, simpleCache);
        }
    }
}
