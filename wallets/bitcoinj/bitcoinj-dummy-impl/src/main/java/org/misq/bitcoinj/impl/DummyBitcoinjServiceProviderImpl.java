package org.misq.bitcoinj.impl;

import org.misq.bitcoinj.api.BitcoinjManager;
import org.misq.bitcoinj.spi.BitcoinjServiceProvider;

public class DummyBitcoinjServiceProviderImpl implements BitcoinjServiceProvider {

    public DummyBitcoinjServiceProviderImpl() {
    }

    @Override
    public BitcoinjManager create() {
        return new DummyBitcoinjManagerImpl();
    }
}
