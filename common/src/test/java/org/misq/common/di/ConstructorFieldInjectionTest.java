package org.misq.common.di;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.misq.common.di.mock.SimpleCache;
import org.misq.common.di.mock.SimpleNode;
import org.misq.common.di.mock.SimpleService;
import org.misq.common.di.module.AbstractDependencyInjectionModule;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConstructorFieldInjectionTest {

    private static SimpleCache simpleCache = new SimpleCache();

    @Test
    @Order(1)
    public void testFieldInjection() {
        // Configure Node A
        DependencyInjector diNodeA = DependencyInjectorConfig.getInjector(new SimpleNodeAModuleConfig());
        SimpleNode nodeA = (SimpleNode) diNodeA.inject(SimpleNode.class);  // injectFieldsIntoClass

        // Configure Node B
        DependencyInjector diNodeB = DependencyInjectorConfig.getInjector(new SimpleNodeBModuleConfig());
        SimpleNode nodeB = (SimpleNode) diNodeB.inject(SimpleNode.class);  // injectFieldsIntoClass

        // Configure Network
        DependencyInjector diNetwork = DependencyInjectorConfig.getInjector(new SimpleNetworkModuleConfig(nodeA));
        SimpleService network = (SimpleService) diNetwork.inject(SimpleService.class);
        assertEquals(nodeA, network.getNode());
        assertEquals(simpleCache, network.getCache());
    }

    private class SimpleNetworkModuleConfig extends AbstractDependencyInjectionModule {

        private final SimpleNode node;

        public SimpleNetworkModuleConfig(SimpleNode node) {
            this.node = node;
        }

        @Override
        public void configure() {
            createMapping(SimpleNode.class, node);
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
