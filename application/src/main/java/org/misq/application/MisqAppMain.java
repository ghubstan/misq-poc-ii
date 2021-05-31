package org.misq.application;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.misq.common.threadmodel.MisqExecutors;
import org.misq.common.threadmodel.UserThread;
import org.misq.grpc.GrpcServer;
import org.misq.web.server.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

// TODO design a clearly understandable app launch path.  This one is based on Bisq's:
//   execute() -> doExecute() -> launchApplication() -> onApplicationLaunched() -> configUserThread() -> setupZuice()

public class MisqAppMain extends MisqExecutable {
    private static final Logger log = LoggerFactory.getLogger(MisqAppMain.class);

    // Asks user if he wants to download & run web server or grpc server (or both),
    // reads reply from cmd line, downloads and dynamically loads modules (I hope).

    @Nullable
    private GrpcServer grpcServer;

    @Nullable
    private WebServer webServer;

    public static void main(String[] args) {
        new MisqAppMain().execute(args);
    }

    public void execute(String[] args) {
        doExecute();
    }

    protected void doExecute() {
        // Note: JavaFX applications need to wait until it is initialized.
        log.debug("doExecute");
        launchApplication();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // First synchronous execution tasks
    /////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void configUserThread() {
        log.debug("configUserThread");
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(this.getClass().getSimpleName())
                .setDaemon(true)
                .build();
        UserThread.setExecutor(MisqExecutors.newSingleThreadExecutor(threadFactory));
    }

    @Override
    protected void launchApplication() {
        log.debug("launchApplication");
        UserThread.execute(this::onApplicationLaunched);
    }

    @Override
    protected void onApplicationLaunched() {
        log.debug("onApplicationLaunched");
        super.onApplicationLaunched();

        startApplication();
    }

    @Override
    public void onSetupComplete() {
        log.debug("onSetupComplete");
    }

    protected void startApplication() {
        // We need to be in user thread!
        log.debug("startApplication");

        // In headless mode we don't have an async behaviour so we trigger the setup by
        // calling onApplicationStarted.
        onApplicationStarted();
    }

    @Override
    protected void onApplicationStarted() {
        log.debug("onApplicationLaunched");
        super.onApplicationStarted();

        startGrpcServer(misqApi);
        startWebServer(misqApi);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                shutdown();
            } catch (Exception ex) {
                log.error("", ex);
            }
        }));
    }

    public void startGrpcServer(MisqApi misqApi) {
        log.info("Starting grpc server...");
        grpcServer = new GrpcServer(misqApi.getCoreApi(),
                misqApi.getWalletInstallerApi());
        grpcServer.start();
    }

    public void startWebServer(MisqApi misqApi) {
        log.info("Starting web server...");
        webServer = new WebServer(misqApi.getCoreApi(),
                misqApi.getWalletInstallerApi());
        webServer.start();
    }

    public void shutdown() {
        if (grpcServer != null) {
            try {
                log.info("Shutting down grpc server...");
                grpcServer.shutdown();
            } catch (Exception ex) {
                log.error("", ex);
            }
        }

        if (webServer != null) {
            try {
                log.info("Shutting down web server...");
                webServer.shutdown();
            } catch (Exception ex) {
                log.error("", ex);
            }
        }

        UserThread.runAfter(() -> System.exit(EXIT_SUCCESS), 1);
    }
}