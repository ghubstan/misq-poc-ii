package org.misq.bitcoinj.spi;

import org.misq.bitcoinj.api.BitcoinjManager;

// SPI for the service
public interface BitcoinjServiceProvider {
    BitcoinjManager create();
}
