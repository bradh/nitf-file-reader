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
package org.codice.imaging.nitf.core;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

import org.codice.imaging.nitf.core.common.AbstractNitfSegmentParser;

/**
    Parser for an image segment subheader in a NITF file.
*/
class NitfImageSegmentHeaderParser extends AbstractNitfSegmentParser {
    private static final int NUM_IMAGE_COORDINATES = 4;
    private static final int COORDINATE_PAIR_LENGTH = NitfConstants.IGEOLO_LENGTH / NUM_IMAGE_COORDINATES;
    private int numImageComments = 0;
    private int numBands = 0;
    private int userDefinedImageDataLength = 0;
    private int imageExtendedSubheaderDataLength = 0;

    private static final Set<ImageCompression> HAS_COMRAT = EnumSet.of(ImageCompression.BILEVEL, ImageCompression.JPEG,
        ImageCompression.VECTORQUANTIZATION, ImageCompression.LOSSLESSJPEG, ImageCompression.JPEG2000, ImageCompression.DOWNSAMPLEDJPEG,
        ImageCompression.BILEVELMASK, ImageCompression.JPEGMASK, ImageCompression.VECTORQUANTIZATIONMASK,
        ImageCompression.LOSSLESSJPEGMASK, ImageCompression.JPEG2000MASK, ImageCompression.USERDEFINED, ImageCompression.USERDEFINEDMASK,
        ImageCompression.ARIDPCM, ImageCompression.ARIDPCMMASK);

    private NitfImageSegmentHeader segment = null;

    NitfImageSegmentHeaderParser() throws ParseException {
    }

    /**
     * Parse the image segment header
     * <p>
     * This will return the image segment header, but it is not threadsafe. Please create a new parser for each header, or protect against
     * parallel runs.
     * @param nitfReader the reader to use to get the data
     * @param parseStrategy the parsing strategy to use to process the data
     * @return the parsed header
     * @throws ParseException on parse failure
     */
    final NitfImageSegmentHeader parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {
        reader = nitfReader;
        segment = new NitfImageSegmentHeader();
        parsingStrategy = parseStrategy;

        readIM();
        readIID1();
        readIDATIM();
        readTGTID();
        readIID2();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readISORCE();
        readNROWS();
        readNCOLS();
        readPVTYPE();
        readIREP();
        readICAT();
        readABPP();
        readPJUST();
        readICORDS();
        if ((segment.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.UNKNOWN)
            && (segment.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE)) {
            readIGEOLO();
        }
        readNICOM();
        for (int i = 0; i < numImageComments; ++i) {
            segment.addImageComment(reader.readTrimmedBytes(NitfConstants.ICOM_LENGTH));
        }
        readIC();
        if (hasCOMRAT()) {
            readCOMRAT();
        }
        readNBANDS();
        if ((reader.getFileType() != FileType.NITF_TWO_ZERO) && (numBands == 0)) {
            readXBANDS();
        }
        for (int i = 0; i < numBands; ++i) {
            NitfImageBand imageBand = new NitfImageBand();
            new NitfImageBandParser(reader, imageBand);
            segment.addImageBand(imageBand);
        }
        readISYNC();
        readIMODE();
        readNBPR();
        readNBPC();
        readNPPBH();
        readNPPBV();
        readNBPP();
        readIDLVL();
        readIALVL();
        readILOC();
        readIMAG();
        readUDIDL();
        if (userDefinedImageDataLength > 0) {
            readUDOFL();
            readUDID();
        }
        readIXSHDL();
        if (imageExtendedSubheaderDataLength > 0) {
            readIXSOFL();
            readIXSHD();
        }
        return segment;
    }

    private Boolean hasCOMRAT() {
        return HAS_COMRAT.contains(segment.getImageCompression());
    }

    private void readIM() throws ParseException {
       reader.verifyHeaderMagic(NitfConstants.IM);
    }

    private void readIID1() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(NitfConstants.IID1_LENGTH));
    }

    private void readIDATIM() throws ParseException {
        segment.setImageDateTime(readNitfDateTime());
    }

    private void readTGTID() throws ParseException {
        segment.setImageTargetId(new TargetId(reader.readBytes(NitfConstants.TGTID_LENGTH)));
    }

    private void readIID2() throws ParseException {
        segment.setImageIdentifier2(reader.readTrimmedBytes(NitfConstants.IID2_LENGTH));
    }

    private void readISORCE() throws ParseException {
        segment.setImageSource(reader.readTrimmedBytes(NitfConstants.ISORCE_LENGTH));
    }

    private void readNROWS() throws ParseException {
        segment.setNumberOfRows(reader.readBytesAsLong(NitfConstants.NROWS_LENGTH));
    }

    private void readNCOLS() throws ParseException {
        segment.setNumberOfColumns(reader.readBytesAsLong(NitfConstants.NCOLS_LENGTH));
    }

    private void readPVTYPE() throws ParseException {
        String pvtype = reader.readTrimmedBytes(NitfConstants.PVTYPE_LENGTH);
        segment.setPixelValueType(PixelValueType.getEnumValue(pvtype));
    }

    private void readIREP() throws ParseException {
        String irep = reader.readTrimmedBytes(NitfConstants.IREP_LENGTH);
        segment.setImageRepresentation(ImageRepresentation.getEnumValue(irep));
    }

    private void readICAT() throws ParseException {
        String icat = reader.readTrimmedBytes(NitfConstants.ICAT_LENGTH);
        segment.setImageCategory(ImageCategory.getEnumValue(icat));
    }

    private void readABPP() throws ParseException {
        segment.setActualBitsPerPixelPerBand(reader.readBytesAsInteger(NitfConstants.ABPP_LENGTH));
    }

    private void readPJUST() throws ParseException {
        String pjust = reader.readTrimmedBytes(NitfConstants.PJUST_LENGTH);
        segment.setPixelJustification(PixelJustification.getEnumValue(pjust));
    }

    private void readICORDS() throws ParseException {
        String icords = reader.readBytes(NitfConstants.ICORDS_LENGTH);
        segment.setImageCoordinatesRepresentation(ImageCoordinatesRepresentation.getEnumValue(icords, reader.getFileType()));
    }

    private void readIGEOLO() throws ParseException {
        ParserFunction<ImageCoordinatePoint> function = null;
        Class clazz = null;

        switch (segment.getImageCoordinatesRepresentation()) {
            case GEOGRAPHIC:
                clazz = ImageGeographicCoordinatePair.class;
                function = ((s -> { ImageGeographicCoordinatePair coords = new ImageGeographicCoordinatePair();
                                    coords.setFromGeographicCoordinates(s);
                                    return coords; }));
                break;
            case GEOCENTRIC:
                clazz = ImageGeographicCoordinatePair.class;
                function = ((s -> { ImageGeographicCoordinatePair coords = new ImageGeographicCoordinatePair();
                                    coords.setFromGeographicCoordinates(s);
                                    return coords; }));
                break;
            case DECIMALDEGREES:
                clazz = ImageDecimalDegreesCoordinatePair.class;
                function = ((s -> { ImageDecimalDegreesCoordinatePair coords = new ImageDecimalDegreesCoordinatePair();
                                    coords.setFromDecimalDegrees(s);
                                    return coords; }));
                break;
            case UTMUPSNORTH:
                clazz = ImageUtmCoordinate.class;
                function = ((s -> { ImageUtmCoordinate coords = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
                                    coords.setFromUtmUps(s);
                                    return coords; }));
                break;
            case UTMUPSSOUTH:
                clazz = ImageUtmCoordinate.class;
                function = ((s -> { ImageUtmCoordinate coords = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSSOUTH);
                    coords.setFromUtmUps(s);
                    return coords; }));
                break;
            case MGRS:
                clazz = ImageMgrsCoordinate.class;
                function = ((s -> { ImageMgrsCoordinate coords = new ImageMgrsCoordinate();
                                    coords.setFromMgrs(s);
                                    return coords; }));
                break;
            default:
                throw new UnsupportedOperationException("NEED TO IMPLEMENT OTHER COORDINATE REPRESENTATIONS: "
                                                        + segment.getImageCoordinatesRepresentation());
        }

        final String igeolo = reader.readBytes(NitfConstants.IGEOLO_LENGTH);
        ImageCoordinates<?> imageCoordinates = handleImageCoordinates(clazz, function, igeolo);
        segment.setImageCoordinates(imageCoordinates);
    }

    private <T extends ImageCoordinatePoint> ImageCoordinates<T> handleImageCoordinates(final Class<T> clazz,
            final ParserFunction<T> func, final String igeolo)
            throws ParseException {
        T[] coords = (T[]) Array.newInstance(clazz, NUM_IMAGE_COORDINATES);

        for (int i = 0; i < NUM_IMAGE_COORDINATES; ++i) {
            String coordStr = igeolo.substring(i * COORDINATE_PAIR_LENGTH, (i + 1) * COORDINATE_PAIR_LENGTH);
            coords[i] = func.apply(coordStr);
        }

        return new ImageCoordinates<T>(coords);
    }

    private void readNICOM() throws ParseException {
        numImageComments = reader.readBytesAsInteger(NitfConstants.NICOM_LENGTH);
    }

    private void readIC() throws ParseException {
        String ic = reader.readBytes(NitfConstants.IC_LENGTH);
        segment.setImageCompression(ImageCompression.getEnumValue(ic));
    }

    private void readNBANDS() throws ParseException {
        numBands = reader.readBytesAsInteger(NitfConstants.NBANDS_LENGTH);
    }

    private void readXBANDS() throws ParseException {
        numBands = reader.readBytesAsInteger(NitfConstants.XBANDS_LENGTH);
    }

    private void readISYNC() throws ParseException {
        reader.readBytes(NitfConstants.ISYNC_LENGTH);
    }

    private void readIMODE() throws ParseException {
        String imode = reader.readBytes(NitfConstants.IMODE_LENGTH);
        segment.setImageMode(ImageMode.getEnumValue(imode));
    }

    private void readNBPR() throws ParseException {
        segment.setNumberOfBlocksPerRow(reader.readBytesAsInteger(NitfConstants.NBPR_LENGTH));
    }

    private void readNBPC() throws ParseException {
        segment.setNumberOfBlocksPerColumn(reader.readBytesAsInteger(NitfConstants.NBPC_LENGTH));
    }

    private void readNPPBH() throws ParseException {
        segment.setNumberOfPixelsPerBlockHorizontal(reader.readBytesAsInteger(NitfConstants.NPPBH_LENGTH));
    }

    private void readNPPBV() throws ParseException {
        segment.setNumberOfPixelsPerBlockVertical(reader.readBytesAsInteger(NitfConstants.NPPBV_LENGTH));
    }

    private void readNBPP() throws ParseException {
        segment.setNumberOfBitsPerPixelPerBand(reader.readBytesAsInteger(NitfConstants.NBPP_LENGTH));
    }

    private void readIDLVL() throws ParseException {
        segment.setImageDisplayLevel(reader.readBytesAsInteger(NitfConstants.IDLVL_LENGTH));
    }

    private void readIALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(NitfConstants.IALVL_LENGTH));
    }

    private void readILOC() throws ParseException {
        segment.setImageLocationRow(reader.readBytesAsInteger(NitfConstants.ILOC_HALF_LENGTH));
        segment.setImageLocationColumn(reader.readBytesAsInteger(NitfConstants.ILOC_HALF_LENGTH));
    }

    private void readIMAG() throws ParseException {
        segment.setImageMagnification(reader.readBytes(NitfConstants.IMAG_LENGTH));
    }

    private void readUDIDL() throws ParseException {
        userDefinedImageDataLength = reader.readBytesAsInteger(NitfConstants.UDIDL_LENGTH);
    }

    private void readUDOFL() throws ParseException {
        segment.setUserDefinedHeaderOverflow(reader.readBytesAsInteger(NitfConstants.UDOFL_LENGTH));
    }

    private void readUDID() throws ParseException {
        TreCollection userDefinedSubheaderTres = parsingStrategy.parseTREs(reader, userDefinedImageDataLength - NitfConstants.UDOFL_LENGTH);
        segment.mergeTREs(userDefinedSubheaderTres);
    }

    private void readIXSHDL() throws ParseException {
        imageExtendedSubheaderDataLength = reader.readBytesAsInteger(NitfConstants.IXSHDL_LENGTH);
    }

    private void readIXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(NitfConstants.IXSOFL_LENGTH));
    }

    private void readIXSHD() throws ParseException {
        TreCollection extendedSubheaderTres = parsingStrategy.parseTREs(reader, imageExtendedSubheaderDataLength - NitfConstants.IXSOFL_LENGTH);
        segment.mergeTREs(extendedSubheaderTres);
    }

    private void readCOMRAT() throws ParseException {
        segment.setCompressionRate(reader.readTrimmedBytes(NitfConstants.COMRAT_LENGTH));
    }
}
