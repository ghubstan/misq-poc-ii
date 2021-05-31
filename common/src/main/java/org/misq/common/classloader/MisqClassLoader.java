package org.misq.common.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

public final class MisqClassLoader extends URLClassLoader {
    private static final Logger log = LoggerFactory.getLogger(MisqClassLoader.class);

    static {
        registerAsParallelCapable();
    }

    public MisqClassLoader(String name, ClassLoader parent) {
        super(name, new URL[0], parent);
    }

    /*
     * Required when this classloader is used as the system classloader.
     */
    public MisqClassLoader(ClassLoader parent) {
        this("classpath", parent);
    }

    public MisqClassLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public void add(URL url) {
        addURL(url);
    }

    @SuppressWarnings("unused")
    public static MisqClassLoader findAncestor(ClassLoader cl) {
        do {
            if (cl instanceof MisqClassLoader)
                return (MisqClassLoader) cl;

            cl = cl.getParent();
        } while (cl != null);

        return null;
    }

    /*
     *  Required for Java Agents when this classloader is used as the system classloader.
     */
    @SuppressWarnings("unused")
    private void appendToClassPathForInstrumentation(String jarfile) throws IOException {
        add(Paths.get(jarfile).toRealPath().toUri().toURL());
    }
}
