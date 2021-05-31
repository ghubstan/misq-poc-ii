package org.misq.api;


import org.misq.common.di.annotation.Inject;
import org.misq.common.di.annotation.Singleton;
import org.misq.p2p.P2PService;
import org.misq.p2p.PeersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CoreApi {
    private static final Logger log = LoggerFactory.getLogger(CoreApi.class);

    private final P2PService p2PService;

    @Inject
    public CoreApi(P2PService p2PService) {
        this.p2PService = p2PService;
    }

    public long getBalance() {
        return 100000000;
    }

    public String getHelp() {
        return "What?";
    }

    public PeersResponse getPeers() {
        PeersResponse peersResponse = p2PService.getPeers();

        log.info(peersResponse.toString());
        log.info("Demonstrate modularized proto defs: {}", peersResponse.toProtoMessage());

        return peersResponse;
    }

    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }
}
