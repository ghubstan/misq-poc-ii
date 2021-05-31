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
public class ZuiceTest {

    private static SimpleCache simpleCache = new SimpleCache();

    @Test
    @Order(1)
    public void testDuplicateFieldInjectionShouldThrowException() {
        Throwable exception = assertThrows(IllegalStateException.class, () -> Zuice.createInjector(new MisConfiguredNodeAModuleConfig()));
        assertEquals("Attempt to cache key/value already present in object map.", exception.getMessage());
    }

    @Test
    @Order(2)
    public void testSingletonInjection() {
        DependencyInjector injectorA = Zuice.createInjector(new SimpleNodeAModuleConfig());
        SimpleNode nodeA = (SimpleNode) injectorA.inject(SimpleNode.class);

        DependencyInjector injectorB = Zuice.createInjector(new SimpleNodeBModuleConfig());
        SimpleNode nodeB = (SimpleNode) injectorB.inject(SimpleNode.class);

        assertEquals(injectorA.getModule().getSingletonMapping(SimpleCache.class),
                injectorB.getModule().getSingletonMapping(SimpleCache.class));

        var singletonA = nodeA.getSimpleCache();
        var singletonB = nodeB.getSimpleCache();
        assertEquals(singletonA, singletonB);

        assertEquals(singletonA,
                injectorB.getModule().getSingletonMapping(SimpleCache.class));
    }

    @Test
    @Order(3)
    public void testFieldInjection() {
        // Configure Node A
        DependencyInjector injectorA = Zuice.createInjector(new SimpleNodeAModuleConfig());
        SimpleNode nodeA = (SimpleNode) injectorA.inject(SimpleNode.class);  // injectFieldsIntoClass
        assertEquals("Node-A", nodeA.getNodeName());
        assertEquals(50_000, nodeA.getPort());
        assertEquals(100, nodeA.getNumRequests());
        assertEquals(simpleCache, nodeA.getSimpleCache());

        // Configure Node B
        DependencyInjector injectorB = Zuice.createInjector(new SimpleNodeBModuleConfig());
        SimpleNode nodeB = (SimpleNode) injectorB.inject(SimpleNode.class);  // injectFieldsIntoClass
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
