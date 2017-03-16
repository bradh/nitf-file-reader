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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;

import static org.codice.imaging.nitf.core.security.impl.FileSecurityConstants.FSCOP_LENGTH;
import static org.codice.imaging.nitf.core.security.impl.FileSecurityConstants.FSCPYS_LENGTH;

/**
    Parser for the file security metadata.
    <p>
    The security metadata at the file level is the same as the subheaders, except for
    two extra fields (copy number, and number of copies).
*/
public class FileSecurityMetadataParser extends SecurityMetadataParser {

    /**
     * default constructor.
     */
    public FileSecurityMetadataParser() {
    }

    /**
     * Parse FileSecurityMetadata from the specified reader.
     *
     * @param nitfReader the NITF source data.
     * @return a FileSecurityMetadata object populated with data parsed from nitfReader.
     * @throws NitfFormatException when the input isn't what was expected.
     */
    public final FileSecurityMetadata parseFileSecurityMetadata(final NitfReader nitfReader) throws NitfFormatException {
        FileSecurityMetadataImpl metadata = new FileSecurityMetadataImpl();
        super.doParse(nitfReader, metadata);
        readFileSecurityMetadataExtras(metadata);
        return metadata;
    }

    private void readFileSecurityMetadataExtras(final FileSecurityMetadataImpl metadata) throws NitfFormatException {
        readFSCOP(metadata);
        readFSCPYS(metadata);
    }

    private void readFSCOP(final FileSecurityMetadataImpl metadata) throws NitfFormatException {
        metadata.setFileCopyNumber(reader.readTrimmedBytes(FSCOP_LENGTH));
    }

    private void readFSCPYS(final FileSecurityMetadataImpl metadata) throws NitfFormatException {
        metadata.setFileNumberOfCopies(reader.readTrimmedBytes(FSCPYS_LENGTH));
    }
};

