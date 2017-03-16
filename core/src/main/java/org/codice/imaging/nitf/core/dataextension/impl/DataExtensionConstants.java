/*
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
 */
package org.codice.imaging.nitf.core.dataextension.impl;

/**
 * Shared data segment values.
 */
public final class DataExtensionConstants {

    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.1
     * <p>
     * See DESID in MIL-STD-2500C Table A-8(A).
     */
    public static final String TRE_OVERFLOW = "TRE_OVERFLOW";

    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.0
     * <p>
     * See MIL-STD-2500A.
     */
    public static final String REGISTERED_EXTENSIONS = "Registered Extensions";

    /**
     * Marker string for Tagged Record Overflow DES in NITF 2.0
     * <p>
     * See MIL-STD-2500A.
     */
    public static final String CONTROLLED_EXTENSIONS = "Controlled Extensions";

    /**
     * DES Marker field for the obsolete streaming mode content.
     */
    public static final String STREAMING_FILE_HEADER = "STREAMING_FILE_HEADER";

    // Data Extenstion Segment (DES)
    /**
     * Marker string for Data Extension Segment (DES) segment header.
     */
    public static final String DE = "DE";

    /**
     * Length of unique DES Type Identifier.
     * <p>
     * MIL-STD-2500C Table A-8(A) / Table A-8(B)
     */
    public static final int DESID_LENGTH = 25;

    /**
     * Length of DES Data Definition version.
     * <p>
     * MIL-STD-2500C Table A-8(A) / Table A-8(B)
     */
    public static final int DESVER_LENGTH = 2;

    /**
     * Length of DES overflowed header type.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    public static final int DESOFLW_LENGTH = 6;

    /**
     * Length of DES Data Overflowed Item field.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    public static final int DESITEM_LENGTH = 3;

    /**
     * Length of the "length of DES-Defined subheader fields" field.
     * <p>
     * MIL-STD-2500C Table A-8(A)
     */
    public static final int DESSHL_LENGTH = 4;

    private DataExtensionConstants() {
    }
}
