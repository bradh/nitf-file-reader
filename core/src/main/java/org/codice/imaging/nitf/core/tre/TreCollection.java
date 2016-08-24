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
package org.codice.imaging.nitf.core.tre;

import java.util.ArrayList;
import java.util.List;

/**
    Collection of TREs.
*/
public class TreCollection {
    private final List<Tre> treCollectionEntries = new ArrayList<>();

    /**
        Return the TREs.

        @return list of TREs
    */
    public final List<Tre> getTREs() {
        return treCollectionEntries;
    }

    /**
        Add a TRE to the collection.

        @param tre the TRE to add to the collection.
    */
    public final void add(final Tre tre) {
        treCollectionEntries.add(tre);
    }

    /**
        Add multiple TREs to the collection.

        @param collectionToAdd the TREs to add.
    */
    public final void add(final TreCollection collectionToAdd) {
        if (collectionToAdd != null) {
            treCollectionEntries.addAll(collectionToAdd.getTREs());
        }
    }

    /**
     * Remove the specified TRE.
     *
     * @param tre the TRE to remove (must be from the list)
     * @return true if the TRE was removed, otherwise false.
     */
    public final boolean remove(final Tre tre) {
        return treCollectionEntries.remove(tre);
    }

    /**
        Get the names of the TREs in the collection.
        <p>
        This method returns a unique list of TRE names. That list can be
        iterated over with getTREsWithName() to get the TREs.

        @return the TRE names.
    */
    public final List<String> getUniqueNamesOfTRE() {
        List<String> treNames = new ArrayList<String>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (!treNames.contains(treName)) {
                treNames.add(treName);
            }
        }
        return treNames;
    }

    /**
        Get the TREs that have a specific name.

        @param nameToMatch the name of the TREs to match.
        @return list of TREs with a specific name.
    */
    public final List<Tre> getTREsWithName(final String nameToMatch) {
        List<Tre> tres = new ArrayList<Tre>();
        for (Tre tre : treCollectionEntries) {
            String treName = tre.getName();
            if (treName.equals(nameToMatch)) {
                tres.add(tre);
            }
        }
        return tres;
    }

    /**
        Check whether this collection has any TREs.

        @return true if there are any TREs, or false if there no TREs in the collection
    */
    public final boolean hasTREs() {
        return !treCollectionEntries.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "TRE Collection";
    }

    /**
     * Get the TREs that came from a specific source.
     *
     * In this context, the source is the part of the header / subheader that
     * the TRE was read from (or where it should be put on write).
     *
     * @param source the source to select for.
     * @return List of TREs that match the source.
     */
    public final List<Tre> getTREsForSource(final TreSource source) {
        List<Tre> tres = new ArrayList<Tre>();
        for (Tre tre : treCollectionEntries) {
            if (tre.getSource() == source) {
                tres.add(tre);
            }
        }
        return tres;
    }
}
