package org.misq.bitcoinj.impl;

import org.misq.bitcoinj.api.BitcoinjManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyBitcoinjManagerImpl implements BitcoinjManager {
    private static final Logger log = LoggerFactory.getLogger(DummyBitcoinjManagerImpl.class);

    @Override
    public void printNetworkParameters() {
        log.info("Dummy NetworkParameters");
    }

    @Override
    public void syncRegtestChain() {
        log.info("Sync dummy regtest chain");
    }

    @Override
    public void shutdownBitcoinj() {
        log.info("Shutdown dummy Bitcoinj");
    }

}
