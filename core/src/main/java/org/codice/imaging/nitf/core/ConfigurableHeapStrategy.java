package org.codice.imaging.nitf.core;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.function.Function;

import org.codice.imaging.nitf.core.common.NitfReader;
import org.slf4j.LoggerFactory;

/**
 * An implementation of HeapStrategy that either stores the data in memory or on disk based
 * on the supplied configuration.
 *
 * @param <R> - The type to be returned by this heap strategy.
 */
public class ConfigurableHeapStrategy<R> implements HeapStrategy<R> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ConfigurableHeapStrategy.class);

    private HeapStrategy<R> fileBackedImageDataStrategy;

    private HeapStrategy<R> inMemoryImageDataStrategy;

    private final HeapStrategyConfiguration heapStrategyConfiguration;

    /**
     * @param dataStrategyConfiguration a HeapStrategyConfiguration which tells this
     *                                  HeapStrategy when to use JVM heap or disk. May
     *                                  not be null.
*      @param fileTFunction a function that will convert a RandomAccessFile into the expected type
     *                      <R>.
     * @param inputStreamTFunction a function that will convert a java.io.InputStream into the
     *                             expected type <R>
     */
    public ConfigurableHeapStrategy(final HeapStrategyConfiguration dataStrategyConfiguration,
            final Function<RandomAccessFile, R> fileTFunction,
            final Function<InputStream, R> inputStreamTFunction) {
        if (dataStrategyConfiguration == null) {
            throw new IllegalArgumentException("ConfigurableHeapStrategy(): argument "
                    + "'heapStrategyConfiguration' may not be null.");
        }

        this.heapStrategyConfiguration = dataStrategyConfiguration;
        this.inMemoryImageDataStrategy = new InMemoryHeapStrategy<>(inputStreamTFunction);
        this.fileBackedImageDataStrategy = new FileBackedHeapStrategy<>(fileTFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final R handleSegment(final NitfReader reader, final long length)
            throws ParseException {
        if (!isRenderable(length)) {
            reader.skip(length);
            return null;
        }

        if (heapStrategyConfiguration.temporaryFilePredicate().test(length)) {
            return fileBackedImageDataStrategy.handleSegment(reader, length);
        } else {
            return inMemoryImageDataStrategy.handleSegment(reader, length);
        }
    }

    /**
     *
     * @param length the length of the image data segment.
     * @return a boolean indicating whether the image data should be set on the ImageSegment.
     */
    public final boolean isRenderable(final long length) {
        return heapStrategyConfiguration.maximumFileSizePredicate().test(length)
                && (getFreeMemory() > length);
    }

    private long getFreeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long allocatedMemory = runtime.totalMemory() - runtime.freeMemory();
        return runtime.maxMemory() - allocatedMemory;
    }
}
