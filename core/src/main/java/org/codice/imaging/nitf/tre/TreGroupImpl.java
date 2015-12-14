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
package org.codice.imaging.nitf.tre;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codice.imaging.nitf.common.tre.TreEntry;
import org.codice.imaging.nitf.common.tre.TreGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    A group of values within a TreEntryImpl.
*/
public class TreGroupImpl implements TreGroup {

    private static final Logger LOG = LoggerFactory.getLogger(TreGroupImpl.class);
    private static final int DECIMAL_BASE = 10;

    private List<TreEntry> entries = new ArrayList<>();

    /**
     {@inheritDoc}
     */
    public final List<TreEntry> getEntries() {
        return entries;
    }

    /**
     {@inheritDoc}
     */
    public final void add(final TreEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     {@inheritDoc}
     */
    public final void addAll(final TreGroup group) {
        if (group != null) {
            entries.addAll(group.getEntries());
        }
    }

    /**
     {@inheritDoc}
     */
    public final void setEntries(final List<TreEntry> treEntries) {
        entries = new ArrayList<>();
        entries.addAll(treEntries);
    }

    /**
     {@inheritDoc}
     */
    public final TreEntry getEntry(final String tagName) throws ParseException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return entry;
            }
        }
        throw new ParseException(String.format("Failed to look up %s", tagName), 0);
    }

    /**
     {@inheritDoc}
     */
    public final String getFieldValue(final String tagName) throws ParseException {
        TreEntry entry = getEntry(tagName);
        return entry.getFieldValue();
    }

    /**
     {@inheritDoc}
     */
    public final int getIntValue(final String tagName) throws ParseException {
        try {
            String fv = getFieldValue(tagName);
            return Integer.parseInt(fv, DECIMAL_BASE);
        } catch (ParseException ex) {
            throw new ParseException(String.format("Failed to look up %s as integer value", tagName), 0);
        }
    }

    /**
     {@inheritDoc}
     */
    public final void dump() {
        for (TreEntry entry : entries) {
            LOG.debug("\t----Start Entry---");
            entry.dump();
            LOG.debug("\t----End Entry---");
        }
    }

    // CSOFF: DesignForExtension
    @Override
    public String toString() {
        return "(Group)";
    }
    // CSON: DesignForExtension
}
