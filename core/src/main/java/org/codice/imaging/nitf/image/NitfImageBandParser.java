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
package org.codice.imaging.nitf.image;

import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.NitfReader;

/**
    Image Band and Image Band LUT parser.
*/
class NitfImageBandParser {
    /**
     * Length of the "nth Band Representation" IREPBANDn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int IREPBAND_LENGTH = 2;

    /**
     * Length of the "nth Band Subcategory" ISUBCATn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int ISUBCAT_LENGTH = 6;

    /**
     * Length of the "nth Band Image Filter Condition" IFCn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int IFC_LENGTH = 1;

    /**
     * Length of the "nth Band Standard Image Filter Code" IMFLTn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int IMFLT_LENGTH = 3;

    /**
     * Length of the "Number of LUTS for the nth Image Band" NLUTSn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int NLUTS_LENGTH = 1;

    /**
     * Length of the "Number of LUT Entries for the nth Image Band" NELUTn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    private static final int NELUT_LENGTH = 5;

    private NitfReader reader = null;
    private NitfImageBand imageBand = null;
    private int numLUTs = 0;

    /**
        Construct from a NitfReader instance.

        @param nitfReader the reader, positioned to read an image band.
        @throws ParseException if an obviously invalid value is detected during parsing,
        or if another problem occurs during parsing (e.g. end of file).
    */
    public NitfImageBandParser(final NitfReader nitfReader, final NitfImageBand band) throws ParseException {
        reader = nitfReader;
        imageBand = band;
        readIREPBAND();
        readISUBCAT();
        readIFC();
        readIMFLT();
        readNLUTS();
        if (numLUTs > 0) {
            readNELUT();
            for (int i = 0; i < numLUTs; ++i) {
                NitfImageBandLUT lut = new NitfImageBandLUT(reader.readBytesRaw(imageBand.getNumLUTEntries()));
                imageBand.addLUT(lut);
            }
        }
    }

    private void readIREPBAND() throws ParseException {
        imageBand.setImageRepresentation(reader.readTrimmedBytes(IREPBAND_LENGTH));
    }

    private void readISUBCAT() throws ParseException {
        imageBand.setImageSubcategory(reader.readTrimmedBytes(ISUBCAT_LENGTH));
    }

    private void readIFC() throws ParseException {
        reader.skip(IFC_LENGTH);
    }

    private void readIMFLT() throws ParseException {
        reader.skip(IMFLT_LENGTH);
    }

    private void readNLUTS() throws ParseException {
        numLUTs = reader.readBytesAsInteger(NLUTS_LENGTH);
    }

    private void readNELUT() throws ParseException {
        imageBand.setNumLUTEntries(reader.readBytesAsInteger(NELUT_LENGTH));
    }
}
