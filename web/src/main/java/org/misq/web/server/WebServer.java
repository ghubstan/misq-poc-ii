package org.misq.web.server;

import io.reactivex.Observable;
import org.misq.api.CoreApi;
import org.misq.wallet.installer.WalletInstallerApi;
import org.misq.web.server.handler.GetBalanceHandler;
import org.misq.web.server.handler.GetPeersHandler;
import org.misq.web.server.handler.GetVersionHandler;
import org.misq.web.server.handler.InstallWalletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.RequestLogger;
import ratpack.registry.Registry;
import ratpack.rx2.RxRatpack;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private RatpackServer ratpackServer;

    private final CoreApi coreApi;
    private final WalletInstallerApi walletInstallerApi;

    public WebServer(CoreApi coreApi, WalletInstallerApi walletInstallerApi) {
        this.coreApi = coreApi;
        this.walletInstallerApi = walletInstallerApi;
    }

    public void start() {
        log.info("start");
        RxRatpack.initialize();
        try {
            ServerConfig serverConfig = ServerConfig.of(config -> config
                    .port(5050)
                    .findBaseDir()
            );
            Registry registrySpec = new MisqRegistrySpec(coreApi, walletInstallerApi).build();
            this.ratpackServer = RatpackServer.start(server -> server
                    .serverConfig(serverConfig)
                    .registry(registrySpec)
                    .handlers(chain -> chain            // Map request paths to request handlers.
                            .all(RequestLogger.ncsa())
                            .get("server-error", ctx -> {
                                Observable.<String>error(new IllegalStateException("Server error from observable"))
                                        .subscribe(s -> {
                                        });
                            })
                            // .get("streaming-movies", ctx -> ctx.get(StreamingMoviesHandler.class).handle(ctx))
                            // .get("bidi-streaming-movies", ctx -> ctx.get(BidiStreamingMoviesHandler.class).handle(ctx))
                            .get("balance", ctx -> ctx.get(GetBalanceHandler.class).handle(ctx))
                            .get("peers", ctx -> ctx.get(GetPeersHandler.class).handle(ctx))
                            .get("version", ctx -> ctx.get(GetVersionHandler.class).handle(ctx))
                            .get("install/wallet", ctx -> ctx.get(InstallWalletHandler.class).handle(ctx))
                            .get(ctx -> ctx.render("Welcome to Misq Web"))
                    )
            );
        } catch (Exception ex) {
            log.error("", ex);
            throw new RuntimeException(ex);
        }
    }

    public void shutdown() {
        if (ratpackServer.isRunning()) {
            try {
                log.info("Server shutdown started");
                ratpackServer.stop();
                log.info("Server shutdown complete");
            } catch (Exception ex) {
                log.error("", ex);
            }
        }
    }
}
