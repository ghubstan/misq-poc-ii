package org.misq.common.di.mock;

import org.misq.common.di.annotation.Inject;

public class SimpleNode {

    @Inject
    private String nodeName;

    @Inject
    private int port;

    @Inject
    private int numRequests;

    @Inject
    private SimpleCache simpleCache;

    public String getNodeName() {
        return nodeName;
    }

    public int getPort() {
        return port;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public SimpleCache getSimpleCache() {
        return simpleCache;
    }
}
