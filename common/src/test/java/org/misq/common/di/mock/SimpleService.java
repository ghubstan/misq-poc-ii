package org.misq.common.di.mock;

import org.misq.common.di.annotation.Inject;
import org.misq.common.di.annotation.Singleton;

@Singleton
public class SimpleService {

    private final SimpleNode node;
    private final SimpleCache cache;

    @Inject
    public SimpleService(SimpleNode node, SimpleCache cache) {
        this.node = node;
        this.cache = cache;
    }

    public SimpleNode getNode() {
        return node;
    }

    public SimpleCache getCache() {
        return cache;
    }
}
