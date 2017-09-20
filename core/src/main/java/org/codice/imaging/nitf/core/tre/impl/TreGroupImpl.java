/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
package org.codice.imaging.nitf.core.tre.impl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreGroupListEntry;
import org.codice.imaging.nitf.core.tre.TreSimpleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    A group of values within a TreGroupListEntry.
*/
class TreGroupImpl implements TreGroup {

    private static final Logger LOG = LoggerFactory.getLogger(TreGroupImpl.class);
    private static final int DECIMAL_BASE = 10;

    private List<TreEntry> entries = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<TreEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Add an entry to the group.
     *
     * @param entry the entry to add
     */
    final void add(final TreEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     * Add multiple entries to the group.
     *
     * @param group the group containing the entry or entries to add
     */
    final void addAll(final TreGroup group) {
        if (group != null) {
            entries.addAll(group.getEntries());
        }
    }

    /**
     * Set the list of entries.
     *
     * @param treEntries the new list of entries.
     */
    public final void setEntries(final List<TreEntry> treEntries) {
        entries = new ArrayList<>();
        entries.addAll(treEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreEntry getEntry(final String tagName) throws NitfFormatException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return entry;
            }
        }
        throw new NitfFormatException(String.format("Failed to look up %s", tagName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreSimpleEntry getSimpleEntry(final String tagName) throws NitfFormatException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return (TreSimpleEntry) entry;
            }
        }
        throw new NitfFormatException(String.format("Failed to look up %s", tagName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreGroupListEntry getGroupListEntry(final String tagName) throws NitfFormatException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName) && (entry instanceof TreGroupListEntry)) {
                return (TreGroupListEntry) entry;
            }
        }
        throw new NitfFormatException(String.format("Failed to look up %s", tagName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFieldValue(final String tagName) throws NitfFormatException {
        TreSimpleEntry entry = getSimpleEntry(tagName);
        return entry.getFieldValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getIntValue(final String tagName) throws NitfFormatException {
        return getBigIntegerValue(tagName).intValueExact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getLongValue(final String tagName) throws NitfFormatException {
        return getBigIntegerValue(tagName).longValueExact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BigInteger getBigIntegerValue(final String tagName) throws NitfFormatException {
        try {
            TreSimpleEntry entry = getSimpleEntry(tagName);
            TreSimpleEntry simpleEntry = (TreSimpleEntry) entry;
            if ("UINT".equals(simpleEntry.getDataType())) {
                return new BigInteger(1, simpleEntry.getFieldValue().getBytes(StandardCharsets.ISO_8859_1));
            } else {
                return new BigInteger(simpleEntry.getFieldValue(), DECIMAL_BASE);
            }
        } catch (NitfFormatException ex) {
            throw new NitfFormatException(String.format("Failed to look up %s as a numerical value", tagName));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getDoubleValue(final String tagName) throws NitfFormatException {
        try {
            TreSimpleEntry entry = getSimpleEntry(tagName);
            return Double.parseDouble(entry.getFieldValue());
        } catch (NitfFormatException ex) {
            throw new NitfFormatException(String.format("Failed to look up %s as double value", tagName));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void dump() {
        for (TreEntry entry : entries) {
            LOG.debug("\t----Start Entry---");
            entry.dump();
            LOG.debug("\t----End Entry---");
        }
    }

    /**
     * {@inheritDoc}
     */
    // CSOFF: DesignForExtension
    @Override
    public String toString() {
        return "(Group)";
    }
    // CSON: DesignForExtension
}
