package org.misq.application;

import org.misq.api.CoreApiFactory;
import org.misq.common.di.DependencyInjector;
import org.misq.common.di.Zuice;
import org.misq.wallet.installer.WalletInstallerApiFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MisqExecutable {
    private static final Logger log = LoggerFactory.getLogger(MisqExecutable.class);

    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    protected MisqApi misqApi;

    protected DependencyInjector injector;

    // The onApplicationLaunched call must map to UserThread, so that all following methods are
    // running in the thread the application is running and we don't run into thread interference.
    protected abstract void launchApplication();

    protected abstract void configUserThread();

    /////////////////////////////////////////////////////////////////////////////////////
    // Note: JavaFX applications need to wait for onApplicationLaunched().
    /////////////////////////////////////////////////////////////////////////////////////

    // Headless versions can manually call inside launchApplication the onApplicationLaunched().
    protected void onApplicationLaunched() {
        configUserThread();
        setupMisqApi();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // We continue with a series of synchronous execution tasks.
    ///////////////////////////////////////////////////////////////////////////////////////////

    protected void setupMisqApi() {
        log.debug("setupZuice");
        ApplicationDependencyInjectionModule appModule = new ApplicationDependencyInjectionModule(
                CoreApiFactory.createCoreApi(),
                WalletInstallerApiFactory.createWalletInstallerApi());
        this.injector = Zuice.createInjector(appModule);
        this.misqApi = (MisqApi) injector.inject(MisqApi.class);
    }

    // Once the application is ready we get that callback and start the setup.
    protected void onApplicationStarted() {
        log.debug("onApplicationStarted");
        runMisqSetup();
    }

    protected void runMisqSetup() {
        log.debug("runMisqSetup");
    }

    public abstract void onSetupComplete();
}
