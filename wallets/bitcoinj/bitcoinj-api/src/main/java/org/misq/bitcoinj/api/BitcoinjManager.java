package org.misq.bitcoinj.api;

public interface BitcoinjManager {
    void printNetworkParameters();

    void syncRegtestChain();

    void shutdownBitcoinj();
}
