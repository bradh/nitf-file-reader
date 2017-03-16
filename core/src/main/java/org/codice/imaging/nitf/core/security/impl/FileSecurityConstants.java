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
package org.codice.imaging.nitf.core.security.impl;

final class FileSecurityConstants {

    private FileSecurityConstants() {
    }

    /**
     * Length of the "File Copy Number" field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FSCOP_LENGTH = 5;

    /**
     * Length of the "File Number of Copies" field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FSCPYS_LENGTH = 5;

}
