package org.misq.common.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.lang.String.format;

public final class MisqJarClassLoader {
    private static final Logger log = LoggerFactory.getLogger(MisqJarClassLoader.class);

    private final MisqClassLoader classLoader;
    private final File jarFile;

    public MisqJarClassLoader(MisqClassLoader classLoader, File jarFile) {
        this.classLoader = classLoader;
        this.jarFile = jarFile;
    }

    public void loadClasses() {
        try {
            URL localJarUrl = jarFile.toURI().toURL();
            List<String> classNames = getJarClassNames(localJarUrl);
            log.info("Loading {} classes from {}", classNames.size(), jarFile.getName());
            classLoader.add(localJarUrl);
            loadClasses(classLoader, classNames);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(
                    format("Could not create a local file URL from %s.", jarFile.getAbsolutePath()), ex);
        }
    }

    private void loadClasses(MisqClassLoader classLoader, List<String> classNames) {
        LinkedHashMap<String, Throwable> classloadingErrors = new LinkedHashMap<>();
        for (String className : classNames) {
            try {
                classLoader.loadClass(className);
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                classloadingErrors.put(className, ex);
            }
        }
        if (classloadingErrors.isEmpty()) {
            log.info("{} classes loaded.", classNames.size());
        } else {
            log.error("Failed class loads:");
            classloadingErrors.forEach((className, throwable) ->
                    log.error("{}:  {} -> {}",
                            className,
                            throwable.getClass().getSimpleName(),
                            throwable.getMessage()));
            throw new IllegalStateException(
                    format("{} classes could not be loaded from %s.",
                            classloadingErrors.size(),
                            jarFile.getAbsolutePath()));
        }
    }

    private List<String> getJarClassNames(URL jarUrl) {
        List<String> classNames = new ArrayList<>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarUrl.getFile()));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replace('/', '.'); // including ".class"
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return classNames;
    }
}
