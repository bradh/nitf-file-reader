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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.ParseStrategy;
import org.codice.imaging.nitf.core.common.impl.AbstractSegmentParser;
import org.codice.imaging.nitf.core.graphic.GraphicColour;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataParser;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
    Parser for a graphic segment in a NITF 2.1 / NSIF 1.0 file.
*/
public class GraphicSegmentParser extends AbstractSegmentParser {

    private int graphicExtendedSubheaderLength = 0;

    private GraphicSegmentImpl segment = null;

    /**
     * Default constructor.
     */
    public GraphicSegmentParser() {
    }

    /**
     * Parse Graphic segment from the specified reader, using the specified parseStrategy.
     *
     * The reader provides the data. The parse strategy selects which data to store.
     *
     * @param nitfReader the NITF input reader.
     * @param parseStrategy the strategy that defines which elements to parse or skip.
     * @param dataLength the length of the segment data part in bytes, excluding the header.
     * @return a fully parsed Graphic segment.
     * @throws NitfFormatException when the parser encounters unexpected input from the reader.
     */
    public final GraphicSegment parse(final NitfReader nitfReader, final ParseStrategy parseStrategy,
            final long dataLength) throws NitfFormatException {
        reader = nitfReader;
        segment = new GraphicSegmentImpl();
        segment.setDataLength(dataLength);
        parsingStrategy = parseStrategy;
        segment.setFileType(nitfReader.getFileType());

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new SecurityMetadataParser().parseSecurityMetadata(reader));
        readENCRYP();
        readSFMT();
        readSSTRUCT();
        readSDLVL();
        readSALVL();
        readSLOC();
        readSBND1();
        readSCOLOR();
        readSBND2();
        readSRES();
        readSXSHDL();
        if (graphicExtendedSubheaderLength > 0) {
            readSXSOFL();
            readSXSHD();
        }
        return segment;
    }

    private void readSY() throws NitfFormatException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws NitfFormatException {
        segment.setIdentifier(reader.readTrimmedBytes(SID_LENGTH));
    }

    private void readSNAME() throws NitfFormatException {
        segment.setGraphicName(reader.readTrimmedBytes(SNAME_LENGTH));
    }

    private void readSFMT() throws NitfFormatException {
        reader.verifyHeaderMagic(SFMT_CGM);
    }

    private void readSSTRUCT() throws NitfFormatException {
        reader.verifyHeaderMagic(SSTRUCT);
    }

    private void readSDLVL() throws NitfFormatException {
        segment.setGraphicDisplayLevel(reader.readBytesAsInteger(SDLVL_LENGTH));
    }

    private void readSALVL() throws NitfFormatException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
    }

    private void readSLOC() throws NitfFormatException {
        segment.setGraphicLocationRow(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setGraphicLocationColumn(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSBND1() throws NitfFormatException {
        segment.setBoundingBox1Row(reader.readBytesAsInteger(SBND1_HALF_LENGTH));
        segment.setBoundingBox1Column(reader.readBytesAsInteger(SBND1_HALF_LENGTH));
    }

    private void readSCOLOR() throws NitfFormatException {
        String scolor = reader.readBytes(SCOLOR_LENGTH);
        segment.setGraphicColour(GraphicColour.getEnumValue(scolor));
    }

    private void readSBND2() throws NitfFormatException {
        segment.setBoundingBox2Row(reader.readBytesAsInteger(SBND2_HALF_LENGTH));
        segment.setBoundingBox2Column(reader.readBytesAsInteger(SBND2_HALF_LENGTH));
    }

    private void readSRES() throws NitfFormatException {
        reader.verifyHeaderMagic(SRES);
    }

    private void readSXSHDL() throws NitfFormatException {
        graphicExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws NitfFormatException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(SXSOFL_LENGTH));
    }

    private void readSXSHD() throws NitfFormatException {
        TreCollection extendedSubheaderTREs = parsingStrategy.parseTREs(reader,
                graphicExtendedSubheaderLength - SXSOFL_LENGTH,
                TreSource.GraphicExtendedSubheaderData);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
