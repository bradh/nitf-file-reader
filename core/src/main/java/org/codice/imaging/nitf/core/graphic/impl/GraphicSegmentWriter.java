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
package org.codice.imaging.nitf.core.graphic.impl;

import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SBND1_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SBND2_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SFMT_CGM;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SRES;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SSTRUCT;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentConstants.SY;

import java.io.DataOutput;
import java.io.IOException;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.codice.imaging.nitf.core.tre.impl.TreParser;

/**
 * Writer for Graphic Segments.
 */
public class GraphicSegmentWriter extends AbstractSegmentWriter {


    /**
     * Constructor.
     *
     * @param output the target to write the graphic segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public GraphicSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the specified graphic segment.
     *
     * @param graphicSegment the segment content to write out
     * @throws IOException on write failure.
     * @throws NitfFormatException on TRE parsing failure.
     */
    public final void writeGraphicSegment(final GraphicSegment graphicSegment) throws IOException, NitfFormatException {
        writeFixedLengthString(SY, SY.length());
        writeFixedLengthString(graphicSegment.getIdentifier(), SID_LENGTH);
        writeFixedLengthString(graphicSegment.getGraphicName(), SNAME_LENGTH);
        writeSecurityMetadata(graphicSegment.getSecurityMetadata());
        writeENCRYP();
        writeFixedLengthString(SFMT_CGM, SFMT_CGM.length());
        writeFixedLengthString(SSTRUCT, SSTRUCT.length());
        writeFixedLengthNumber(graphicSegment.getGraphicDisplayLevel(), SDLVL_LENGTH);
        writeFixedLengthNumber(graphicSegment.getAttachmentLevel(), SALVL_LENGTH);
        writeFixedLengthNumber(graphicSegment.getGraphicLocationRow(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(graphicSegment.getGraphicLocationColumn(), SLOC_HALF_LENGTH);
        writeFixedLengthNumber(graphicSegment.getBoundingBox1Row(), SBND1_HALF_LENGTH);
        writeFixedLengthNumber(graphicSegment.getBoundingBox1Column(), SBND1_HALF_LENGTH);
        writeFixedLengthString(graphicSegment.getGraphicColour().getTextEquivalent(), SCOLOR_LENGTH);
        writeFixedLengthNumber(graphicSegment.getBoundingBox2Row(), SBND2_HALF_LENGTH);
        writeFixedLengthNumber(graphicSegment.getBoundingBox2Column(), SBND2_HALF_LENGTH);
        writeFixedLengthString(SRES, SRES.length()); // SRES2
        byte[] graphicExtendedSubheaderData = mTreParser.getTREs(graphicSegment, TreSource.GraphicExtendedSubheaderData);
        int graphicExtendedSubheaderDataLength = graphicExtendedSubheaderData.length;
        if ((graphicExtendedSubheaderDataLength > 0) || (graphicSegment.getExtendedHeaderDataOverflow() != 0)) {
            graphicExtendedSubheaderDataLength += SXSOFL_LENGTH;
        }
        writeFixedLengthNumber(graphicExtendedSubheaderDataLength, SXSHDL_LENGTH);
        if (graphicExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(graphicSegment.getExtendedHeaderDataOverflow(), SXSOFL_LENGTH);
            writeBytes(graphicExtendedSubheaderData, graphicExtendedSubheaderDataLength - SXSOFL_LENGTH);
        }
        writeSegmentData(graphicSegment.getData());
    }

}
