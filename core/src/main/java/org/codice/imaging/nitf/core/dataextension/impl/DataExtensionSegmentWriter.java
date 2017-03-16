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

import java.io.DataOutput;
import java.io.IOException;
import org.codice.imaging.nitf.core.common.impl.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DE;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DESID_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DESITEM_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DESOFLW_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DESSHL_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionConstants.DESVER_LENGTH;

import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.tre.impl.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for Data Extension Segments (DESs).
 */
public class DataExtensionSegmentWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param output the target to write the data extension segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public DataExtensionSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the subheader information for this data extension segment.
     *
     * @param des the header to write
     * @throws IOException on write failure
     * @throws NitfFormatException on TRE parse problems
     */
    public final void writeDESHeader(final DataExtensionSegment des) throws IOException, NitfFormatException {
        writeFixedLengthString(DE, DE.length());
        writeFixedLengthString(des.getIdentifier(), DESID_LENGTH);
        writeFixedLengthNumber(des.getDESVersion(), DESVER_LENGTH);
        writeSecurityMetadata(des.getSecurityMetadata());
        if (des.isTreOverflow()) {
            writeFixedLengthString(des.getOverflowedHeaderType(), DESOFLW_LENGTH);
            writeFixedLengthNumber(des.getItemOverflowed(), DESITEM_LENGTH);
        }
        writeFixedLengthNumber(des.getUserDefinedSubheaderField().length(), DESSHL_LENGTH);
        if (des.getUserDefinedSubheaderField().length() > 0) {
            mOutput.writeBytes(des.getUserDefinedSubheaderField());
        } else {
            byte[] treData = mTreParser.getTREs(des, TreSource.TreOverflowDES);
            mOutput.write(treData);
        }
        des.consume(this::writeSegmentData);
    }
}
