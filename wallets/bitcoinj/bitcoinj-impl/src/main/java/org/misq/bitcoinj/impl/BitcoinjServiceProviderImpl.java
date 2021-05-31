package org.misq.bitcoinj.impl;

import org.misq.bitcoinj.api.BitcoinjManager;
import org.misq.bitcoinj.spi.BitcoinjServiceProvider;

public class BitcoinjServiceProviderImpl implements BitcoinjServiceProvider {

    public BitcoinjServiceProviderImpl() {
    }

    @Override
    public BitcoinjManager create() {
        return new BitcoinjManagerImpl();
    }
}
