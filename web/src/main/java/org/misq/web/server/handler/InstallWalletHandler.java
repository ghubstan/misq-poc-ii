package org.misq.web.server.handler;


import org.misq.wallet.installer.WalletInstallerApi;
import org.misq.web.json.JsonTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import static org.misq.wallet.installer.WalletInstallerApi.CONCURRENCY_FRWK.RX_OBSERVABLE;


public class InstallWalletHandler extends AbstractHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(InstallWalletHandler.class);

    public InstallWalletHandler(JsonTransform jsonTransform) {
        super(jsonTransform);
    }

    @Override
    public void handle(Context ctx) {
        ctx.render(toJson("status", "Downloading Bitcoinj jar, loading classes, initializing and shutting down wallet."));
        try {
            WalletInstallerApi walletInstallerApi = ctx.get(WalletInstallerApi.class);
            walletInstallerApi.installBitcoinj(RX_OBSERVABLE);
        } catch (Throwable t) {
            log.error("", t);
        }
    }
}
