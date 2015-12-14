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
package org.codice.imaging.nitf.symbol;

import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.graphic.NitfGraphicSegmentConstants.SY;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.parser.AbstractNitfSegmentParser;
import org.codice.imaging.nitf.security.NitfSecurityMetadataImpl;
import org.codice.imaging.nitf.parser.strategy.NitfParseStrategy;
import org.codice.imaging.nitf.tre.TreCollectionImpl;

/**
    Parser for a symbol segment subheader in a NITF 2.0 file.
*/
public class NitfSymbolSegmentHeaderParser extends AbstractNitfSegmentParser {

    // Symbol (Graphic) Segment
    /**
     * Length of the "Symbol Type" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int SYTYPE_LENGTH = 1;

    /**
     * Length of the "Number of Lines Per Symbol" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int NLIPS_LENGTH = 4;

    /**
     * Length of the "Number of Pixels Per Line" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int NPIXPL_LENGTH = 4;

    /**
     * Length of the "Line Width" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int NWDTH_LENGTH = 4;

    /**
     * Length of the "Number of Bits Per Pixel" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int SYNBPP_LENGTH = 1;

    /**
     * Length of the "Symbol Number" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int SNUM_LENGTH = 6;

    /**
     * Length of the "Symbol Rotation" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int SROT_LENGTH = 3;

    /**
     * Length of the "Number of LUT Entries" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    private static final int SYNELUT_LENGTH = 3;


    private int numberOfEntriesInLUT = 0;
    private int symbolExtendedSubheaderLength = 0;

    private NitfSymbolSegmentHeader segment = null;

    /**
     *
     * @param nitfReader - the NitfReader that will collect incoming symbols.
     * @param parseStrategy - the ParseStrategy to use.
     * @return The NitfSymbolSegmentHeader.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    public final NitfSymbolSegmentHeader parse(final NitfReader nitfReader,
            final NitfParseStrategy parseStrategy) throws ParseException {

        reader = nitfReader;
        segment = new NitfSymbolSegmentHeader();
        parsingStrategy = parseStrategy;

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadataImpl(reader));
        readENCRYP();
        readSTYPE();
        readNLIPS();
        readNPIXPL();
        readNWDTH();
        readNBPP();
        readSDLVL();
        readSALVL();
        readSLOC();
        readSLOC2();
        readSCOLOR();
        readSNUM();
        readSROT();
        readNELUT();
        for (int i = 0; i < numberOfEntriesInLUT; ++i) {
            throw new UnsupportedOperationException("TODO: Implement LUT parsing when we have an example");
        }
        readSXSHDL();
        if (symbolExtendedSubheaderLength > 0) {
            readSXSOFL();
            readSXSHD();
        }
        return segment;
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(SID_LENGTH));
    }

    private void readSNAME() throws ParseException {
        segment.setSymbolName(reader.readTrimmedBytes(SNAME_LENGTH));
    }

    private void readSTYPE() throws ParseException {
        String stype = reader.readTrimmedBytes(SYTYPE_LENGTH);
        segment.setSymbolType(SymbolType.getEnumValue(stype));
    }

    private void readNLIPS() throws ParseException {
        segment.setNumberOfLinesPerSymbol(reader.readBytesAsInteger(NLIPS_LENGTH));
    }

    private void readNPIXPL() throws ParseException {
        segment.setNumberOfPixelsPerLine(reader.readBytesAsInteger(NPIXPL_LENGTH));
    }

    private void readNWDTH() throws ParseException {
        segment.setLineWidth(reader.readBytesAsInteger(NWDTH_LENGTH));
    }

    private void readNBPP() throws ParseException {
        segment.setNumberOfBitsPerPixel(reader.readBytesAsInteger(SYNBPP_LENGTH));
    }

    private void readSDLVL() throws ParseException {
        segment.setSymbolDisplayLevel(reader.readBytesAsInteger(SDLVL_LENGTH));
    }

    private void readSALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
    }

    private void readSLOC() throws ParseException {
        segment.setSymbolLocationRow(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocationColumn(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSLOC2() throws ParseException {
        segment.setSymbolLocation2Row(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocation2Column(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readTrimmedBytes(SCOLOR_LENGTH);
        segment.setSymbolColourFormat(SymbolColour.getEnumValue(scolor));
    }

    private void readSNUM() throws ParseException {
        segment.setSymbolNumber(reader.readBytes(SNUM_LENGTH));
    }

    private void readSROT() throws ParseException {
        segment.setSymbolRotation(reader.readBytesAsInteger(SROT_LENGTH));
    }

    private void readNELUT() throws ParseException {
        numberOfEntriesInLUT = reader.readBytesAsInteger(SYNELUT_LENGTH);
    }

    private void readSXSHDL() throws ParseException {
        symbolExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(SXSOFL_LENGTH));
    }

    private void readSXSHD() throws ParseException {
        TreCollectionImpl extendedSubheaderTREs = parsingStrategy.parseTREs(reader, symbolExtendedSubheaderLength - SXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
