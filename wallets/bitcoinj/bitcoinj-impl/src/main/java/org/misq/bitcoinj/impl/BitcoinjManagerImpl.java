package org.misq.bitcoinj.impl;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.utils.BriefLogFormatter;
import org.misq.bitcoinj.api.BitcoinjManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BitcoinjManagerImpl implements BitcoinjManager {
    private static final Logger log = LoggerFactory.getLogger(BitcoinjManagerImpl.class);

    private WalletAppKit kit;

    @Override
    public void printNetworkParameters() {
        log.info("Real NetworkParameters:");
        log.info("\t{}", RegTestParams.get().toString());
        log.info("\t{}", TestNet3Params.get().toString());
        log.info("\t{}", MainNetParams.get().toString());
    }

    @Override
    public void syncRegtestChain() {
        log.info("Sync real regtest chain");
        configBitcoinjLogger();
        sync(RegTestParams.get(), new File("."), "spi-wallet");
    }

    private void configBitcoinjLogger() {
        log.info("\tInit BriefLogFormatter");
        BriefLogFormatter.init();
    }

    private void sync(NetworkParameters networkParameters, File walletDir, String filePrefix) {
        // Start up a basic app using a class that automates some boilerplate. Ensure we always have at least one key.
        this.kit = new WalletAppKit(networkParameters, walletDir, filePrefix) {
            @Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, as setting up various objects
                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                // on the main thread.
                if (wallet().getKeyChainGroupSize() < 1)
                    wallet().importKey(new ECKey());
            }
        };

        if (networkParameters == RegTestParams.get()) {
            // Regression test mode is designed for testing and development only, so there's no public network for it.
            // If you pick this mode, you're expected to be running a local "bitcoind -regtest" instance.
            kit.connectToLocalHost();
        }

        // Startup is considered complete once the network activity begins and
        // peer connections/block chain sync will continue in the background.
        kit.setBlockingStartup(false);
        kit.startAsync();
        kit.awaitRunning();
        log.info("Chain is downloaded");
    }

    @Override
    public void shutdownBitcoinj() {
        log.info("Shutdown Bitcoinj");
        kit.stopAsync();
        kit.awaitTerminated();
    }

}
