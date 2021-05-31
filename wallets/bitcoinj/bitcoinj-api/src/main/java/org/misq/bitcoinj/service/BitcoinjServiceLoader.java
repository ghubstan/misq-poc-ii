package org.misq.bitcoinj.service;

import org.misq.bitcoinj.exception.ProviderNotFoundException;
import org.misq.bitcoinj.spi.BitcoinjServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

// Utility class that can be used by client code.  Delegates to ServiceLoader.
public class BitcoinjServiceLoader {

    // Should these be private?
    public static final String DUMMY_BITCOINJ_SERVICE_PROVIDER = "org.misq.bitcoinj.impl.DummyBitcoinjServiceProviderImpl";
    public static final String DYNAMIC_BITCOINJ_SERVICE_PROVIDER = "org.misq.bitcoinj.impl.BitcoinjServiceProviderImpl";

    /**
     * Return the default BitcoinjServiceProvider, which should be loaded by the AppClassLoader at startup.
     * TODO Verify some dummy service provider impl has to be loaded at startup;  maybe it does not.
     *
     * @return BitcoinjServiceProvider
     */
    @SuppressWarnings("unused")
    public static BitcoinjServiceProvider provider() {
        return provider(DUMMY_BITCOINJ_SERVICE_PROVIDER);
    }

    /**
     * Returns the BitcoinjServiceProvider with the given name.
     *
     * @param providerName the fully qualified name of the service provider implementation, e.g.,
     *                     org.misq.bitcoinj.impl.DummyBitcoinjServiceProviderImpl
     * @return BitcoinjServiceProvider
     * @throws ProviderNotFoundException
     */
    public static BitcoinjServiceProvider provider(String providerName) {
        return provider(providerName, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Returns the BitcoinjServiceProvider with the given name, from the given class loader.
     *
     * @param providerName the fully qualified name of the service provider implementation, e.g.,
     *                     org.misq.bitcoinj.impl.BitcoinjServiceProviderImpl
     * @param classLoader  the classloader that dynamically loaded the service provider implementation classes
     * @return BitcoinjServiceProvider
     * @throws ProviderNotFoundException
     */
    public static BitcoinjServiceProvider provider(String providerName, ClassLoader classLoader) {
        ServiceLoader<BitcoinjServiceProvider> serviceLoader = ServiceLoader.load(BitcoinjServiceProvider.class, classLoader);
        ServiceLoader.Provider<BitcoinjServiceProvider> serviceProvider = serviceLoader.stream()
                .filter(spi -> spi.get().getClass().getName().equals(providerName))
                .findFirst().orElseThrow(() ->
                        new ProviderNotFoundException("Service provider " + providerName + " not found"));
        return serviceProvider.get();
    }

    /**
     * Returns all available service providers.
     *
     * @return List<BitcoinjServiceProvider>
     */
    @SuppressWarnings("unused")
    public static List<BitcoinjServiceProvider> providers() {
        List<BitcoinjServiceProvider> services = new ArrayList<>();
        ServiceLoader<BitcoinjServiceProvider> loader = ServiceLoader.load(BitcoinjServiceProvider.class);
        loader.forEach(services::add);
        return services;
    }
}
