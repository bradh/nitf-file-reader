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
package org.codice.imaging.nitf.parser.strategy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.codice.imaging.nitf.common.FileType;
import org.codice.imaging.nitf.common.Nitf;
import org.codice.imaging.nitf.common.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.dataextension.NitfDataExtensionSegmentHeaderParser;
import org.codice.imaging.nitf.graphic.NitfGraphicSegmentHeader;
import org.codice.imaging.nitf.graphic.NitfGraphicSegmentHeaderParser;
import org.codice.imaging.nitf.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.image.NitfImageSegmentHeaderParser;
import org.codice.imaging.nitf.label.NitfLabelSegmentHeader;
import org.codice.imaging.nitf.label.NitfLabelSegmentHeaderParser;
import org.codice.imaging.nitf.symbol.NitfSymbolSegmentHeader;
import org.codice.imaging.nitf.symbol.NitfSymbolSegmentHeaderParser;
import org.codice.imaging.nitf.text.NitfTextSegmentHeader;
import org.codice.imaging.nitf.text.NitfTextSegmentHeaderParser;
import org.codice.imaging.nitf.tre.TreCollectionImpl;
import org.codice.imaging.nitf.tre.TreCollectionParser;

/**
 * "Slotted" parse strategy.
 * This could probably be split into
 * an abstract parent called SlottedNitfParseStrategy
 * and
 * the abstract subclass with the data storage (SlottedStorageNitfParseStrategy)
 */
public abstract class SlottedNitfParseStrategy implements NitfParseStrategy {
    /**
     * The file level header.
     */
    protected Nitf nitfFileLevelHeader;
    /**
     * The list of image segment headers.
     */
    protected final List<NitfImageSegmentHeader> imageSegmentHeaders = new ArrayList<>();
    /**
     * The list of image segment data.
     */
    protected final List<byte[]> imageSegmentData = new ArrayList<>();
    /**
     * The list of image segment headers.
     */
    protected final List<NitfGraphicSegmentHeader> graphicSegmentHeaders = new ArrayList<>();
    /**
     * The list of image segment data.
     */
    protected final List<byte[]> graphicSegmentData = new ArrayList<>();
    /**
     * The list of symbol segment headers.
     */
    protected final List<NitfSymbolSegmentHeader> symbolSegmentHeaders = new ArrayList<>();
    /**
     * The list of symbol segment data.
     */
    protected final List<byte[]> symbolSegmentData = new ArrayList<>();
    /**
     * The list of label segment headers.
     */
    protected final List<NitfLabelSegmentHeader> labelSegmentHeaders = new ArrayList<>();
    /**
     * The list of label segment data.
     */
    protected final List<String> labelSegmentData = new ArrayList<>();
    /**
     * The list of text segment headers.
     */
    protected final ArrayList<NitfTextSegmentHeader> textSegmentHeaders = new ArrayList<>();
    /**
     * The list of text segment data.
     */
    protected final ArrayList<String> textSegmentData = new ArrayList<>();
    /**
     * The list of DES headers.
     */
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegmentHeaders = new ArrayList<>();
    /**
     * The list of DES data.
     */
    protected final List<byte[]> dataExtensionSegmentData = new ArrayList<>();

    /**
     * The TRE parser to use. Must be initialised before use, see initialiseTreCollectionParserIfRequired()
     */
    protected TreCollectionParser treCollectionParser;

    /**
     * Constructor.
     */
    public SlottedNitfParseStrategy() {
        treCollectionParser = null;
    }

    @Override
    public final void setFileHeader(final Nitf nitf) {
        nitfFileLevelHeader = nitf;
    }

    @Override
    public final Nitf getNitfHeader() {
        return nitfFileLevelHeader;
    }

    /**
     * Return the list of image segment data.
     *
     * @return image segment data
     */
    public final List<byte[]> getImageSegmentData() {
        return imageSegmentData;
    }

    /**
     * Return the list of symbol segment data.
     *
     * @return symbol segment data
     */
    public final List<byte[]> getSymbolSegmentData() {
        return symbolSegmentData;
    }

    /**
     * Return list of label segment data.
     *
     * @return label segment data
     */
    public final List<String> getLabelSegmentData() {
        return labelSegmentData;
    }

    /**
     * Return list of image segment data.
     *
     * @return image segment data
     */
    public final List<byte[]> getGraphicSegmentData() {
        return graphicSegmentData;
    }

    /**
     * Return list of text segment data.
     *
     * @return text segment data
     */
    public final List<String> getTextSegmentData() {
        return textSegmentData;
    }

    @Override
    public final void baseHeadersRead(final NitfReader reader) {
        try {
            for (int i = 0; i < nitfFileLevelHeader.getImageSegmentSubHeaderLengths().size(); ++i) {
                handleImageSegment(reader, i);
            }
            if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
                for (int i = 0; i < nitfFileLevelHeader.getSymbolSegmentSubHeaderLengths().size(); ++i) {
                    handleSymbolSegment(reader, i);
                }
                for (int i = 0; i < nitfFileLevelHeader.getLabelSegmentSubHeaderLengths().size(); ++i) {
                   handleLabelSegment(reader, i);
                }
            } else {
                for (int i = 0; i < nitfFileLevelHeader.getGraphicSegmentSubHeaderLengths().size(); ++i) {
                   handleGraphicSegment(reader, i);
                }
            }
            for (int i = 0; i < nitfFileLevelHeader.getTextSegmentSubHeaderLengths().size(); ++i) {
                handleTextSegment(reader, i);
            }
            for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
                handleDataExtensionSegment(reader, i);
            }
        } catch (ParseException ex) {
            System.out.println("Exception should be logged: " + ex);
        }
    }


//<editor-fold defaultstate="collapsed" desc="Image segment methods">
    /**
     * Parse the image segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseImageSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        imageSegmentHeaders.add(imageSegmentHeader);
        byte[] data = readImageSegmentData(imageSegmentHeader, reader);
        imageSegmentData.add(data);
    }

    /**
     * Parse the image segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseImageSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeader imageSegmentHeader = readImageSegmentHeader(reader, i);
        skipImageSegmentData(imageSegmentHeader, reader);
        imageSegmentHeaders.add(imageSegmentHeader);
    }

    /**
     * Parse the image segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfImageSegmentHeader readImageSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfImageSegmentHeaderParser imageSegmentHeaderParser = new NitfImageSegmentHeaderParser();
        NitfImageSegmentHeader imageSegmentHeader = imageSegmentHeaderParser.parse(reader, this);
        imageSegmentHeader.setImageSegmentDataLength(nitfFileLevelHeader.getImageSegmentDataLengths().get(i));
        return imageSegmentHeader;
    }

    private void initialiseTreCollectionParserIfRequired() throws ParseException {
        if (treCollectionParser == null) {
            treCollectionParser = new TreCollectionParser();
        }
    }

    /**
     * Read the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param imageSegmentHeader the header for the image segment that is to be read
     * @param reader the reader to use to read the data.
     * @return byte array of the data, or a null pointer if the data length was zero
     * @throws ParseException on failure.
     */
    protected final byte[] readImageSegmentData(final NitfImageSegmentHeader imageSegmentHeader, final NitfReader reader) throws ParseException {
        if (imageSegmentHeader.getImageDataLength() > 0) {
            return reader.readBytesRaw((int) imageSegmentHeader.getImageDataLength());
        }
        return null;
    }

    /**
     * Skip the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param imageSegmentHeader the header for the image segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipImageSegmentData(final NitfImageSegmentHeader imageSegmentHeader, final NitfReader reader) throws ParseException {
        if (imageSegmentHeader.getImageDataLength() > 0) {
            reader.skip(imageSegmentHeader.getImageDataLength());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Graphic segment methods">
    /**
     * Parse the image segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseGraphicSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        graphicSegmentHeaders.add(graphicSegmentHeader);
        byte[] data = readGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentData.add(data);
    }

    /**
     * Parse the image segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseGraphicSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeader graphicSegmentHeader = readGraphicSegmentHeader(reader, i);
        skipGraphicSegmentData(graphicSegmentHeader, reader);
        graphicSegmentHeaders.add(graphicSegmentHeader);
    }

    /**
     * Parse the image segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfGraphicSegmentHeader readGraphicSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfGraphicSegmentHeaderParser graphicSegmentHeaderParser = new NitfGraphicSegmentHeaderParser();
        NitfGraphicSegmentHeader graphicSegmentHeader = graphicSegmentHeaderParser.parse(reader, this);
        graphicSegmentHeader.setGraphicSegmentDataLength(nitfFileLevelHeader.getGraphicSegmentDataLengths().get(i));
        return graphicSegmentHeader;
    }

    /**
     * Read the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param graphicSegmentHeader the header for the image segment that is to be read
     * @param reader the reader to use to read the data.
     * @return byte array of the segment data, or null if there was no data
     * @throws ParseException on failure.
     */
    protected final byte[] readGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegmentHeader,
            final NitfReader reader) throws ParseException {
        if (graphicSegmentHeader.getGraphicDataLength() > 0) {
            return reader.readBytesRaw(graphicSegmentHeader.getGraphicDataLength());
        }
        return null;
    }

    /**
     * Skip the image segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param graphicSegmentHeader the header for the image segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipGraphicSegmentData(final NitfGraphicSegmentHeader graphicSegmentHeader, final NitfReader reader) throws ParseException {
        if (graphicSegmentHeader.getGraphicDataLength() > 0) {
            reader.skip(graphicSegmentHeader.getGraphicDataLength());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Symbol segment methods">
    /**
     * Parse the symbol segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseSymbolSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        symbolSegmentHeaders.add(symbolSegmentHeader);
        byte[] data = readSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentData.add(data);
    }

    /**
     * Parse the symbol segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseSymbolSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeader symbolSegmentHeader = readSymbolSegmentHeader(reader, i);
        skipSymbolSegmentData(symbolSegmentHeader, reader);
        symbolSegmentHeaders.add(symbolSegmentHeader);
    }

    /**
     * Parse the symbol segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfSymbolSegmentHeader readSymbolSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfSymbolSegmentHeaderParser symbolSegmentHeaderParser = new NitfSymbolSegmentHeaderParser();
        NitfSymbolSegmentHeader symbolSegmentHeader = symbolSegmentHeaderParser.parse(reader, this);
        symbolSegmentHeader.setSymbolSegmentDataLength(nitfFileLevelHeader.getSymbolSegmentDataLengths().get(i));
        return symbolSegmentHeader;
    }

    /**
     * Read the symbol segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param symbolSegmentHeader the header for the symbol segment that is to be read
     * @param reader the reader to use to read the data.
     * @return the data for the symbol, or null if no data was present
     * @throws ParseException on failure.
     */
    protected final byte[] readSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegmentHeader, final NitfReader reader) throws ParseException {
        if (symbolSegmentHeader.getSymbolDataLength() > 0) {
            return reader.readBytesRaw(symbolSegmentHeader.getSymbolDataLength());
        }
        return null;
    }

    /**
     * Skip the symbol segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param symbolSegmentHeader the header for the symbol segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipSymbolSegmentData(final NitfSymbolSegmentHeader symbolSegmentHeader, final NitfReader reader) throws ParseException {
        if (symbolSegmentHeader.getSymbolDataLength() > 0) {
            reader.skip(symbolSegmentHeader.getSymbolDataLength());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Label Segment Methods">
    /**
     * Parse the label segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseLabelSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        labelSegmentHeaders.add(labelSegmentHeader);
        String labelData = readLabelSegmentData(labelSegmentHeader, reader);
        labelSegmentData.add(labelData);
    }

    /**
     * Parse the label segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseLabelSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeader labelSegmentHeader = readLabelSegmentHeader(reader, i);
        labelSegmentHeaders.add(labelSegmentHeader);
        skipLabelSegmentData(labelSegmentHeader, reader);
    }

    /**
     * Parse the label segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfLabelSegmentHeader readLabelSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfLabelSegmentHeaderParser labelSegmentHeaderParser = new NitfLabelSegmentHeaderParser();
        NitfLabelSegmentHeader labelSegmentHeader = labelSegmentHeaderParser.parse(reader, this);
        labelSegmentHeader.setLabelSegmentDataLength(nitfFileLevelHeader.getLabelSegmentDataLengths().get(i));
        return labelSegmentHeader;
    }

    /**
     * Read the label segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param labelSegmentHeader the header for the label segment that is to be read
     * @param reader the reader to use to read the data.
     * @return string containing the label segment data
     * @throws ParseException on failure.
     */
    protected final String readLabelSegmentData(final NitfLabelSegmentHeader labelSegmentHeader, final NitfReader reader) throws ParseException {
        if (labelSegmentHeader.getLabelDataLength() > 0) {
            return reader.readBytes(labelSegmentHeader.getLabelDataLength());
        }
        return "";
    }

    /**
     * Skip the label segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param labelSegmentHeader the header for the label segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipLabelSegmentData(final NitfLabelSegmentHeader labelSegmentHeader, final NitfReader reader) throws ParseException {
        if (labelSegmentHeader.getLabelDataLength() > 0) {
            reader.skip(labelSegmentHeader.getLabelDataLength());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Text segment methods">
    /**
     * Parse the text segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseTextSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        textSegmentHeaders.add(textSegmentHeader);
        String text = readTextSegmentData(textSegmentHeader, reader);
        textSegmentData.add(text);
    }

    /**
     * Parse the text segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseTextSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeader textSegmentHeader = readTextSegmentHeader(reader, i);
        textSegmentHeaders.add(textSegmentHeader);
        skipTextSegmentData(textSegmentHeader, reader);
    }

    /**
     * Parse the text segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfTextSegmentHeader readTextSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfTextSegmentHeaderParser textSegmentHeaderParser = new NitfTextSegmentHeaderParser();
        NitfTextSegmentHeader textSegmentHeader = textSegmentHeaderParser.parse(reader, this);
        textSegmentHeader.setTextSegmentDataLength(nitfFileLevelHeader.getTextSegmentDataLengths().get(i));
        return textSegmentHeader;
    }

    /**
     * Read the text segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param textSegmentHeader the header for the text segment that is to be read
     * @param reader the reader to use to read the data.
     * @return a string containing the text segment data, or an empty string if there is no data.
     * @throws ParseException on failure.
     */
    protected final String readTextSegmentData(final NitfTextSegmentHeader textSegmentHeader, final NitfReader reader) throws ParseException {
        if (textSegmentHeader.getTextDataLength() > 0) {
            return reader.readBytes(textSegmentHeader.getTextDataLength());
        }
        return "";
    }

    /**
     * Skip the text segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param textSegmentHeader the header for the text segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipTextSegmentData(final NitfTextSegmentHeader textSegmentHeader, final NitfReader reader) throws ParseException {
        if (textSegmentHeader.getTextDataLength() > 0) {
            reader.skip(textSegmentHeader.getTextDataLength());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DES methods">
    /**
     * Parse the data extension segment header and associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseDataExtensionSegmentHeaderAndData(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = readDataExtensionSegmentHeader(reader, i);
        dataExtensionSegmentHeaders.add(dataExtensionSegmentHeader);
        readDataExtensionSegmentData(dataExtensionSegmentHeader, reader);
    }

    /**
     * Parse the data extension segment header and skip over the associated data.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header / data segment to read (zero base)
     * @throws ParseException on parse error
     */
    protected final void parseDataExtensionSegmentHeaderButSkipData(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = readDataExtensionSegmentHeader(reader, i);
        skipDataExtensionSegmentData(dataExtensionSegmentHeader, reader);
        dataExtensionSegmentHeaders.add(dataExtensionSegmentHeader);
    }

    /**
     * Parse the data extension segment header.
     *
     * The reader will be positioned at the start of the associated data segment.
     *
     * @param reader Reader to use for reading
     * @param i the index of the header to read (zero base)
     * @return the segment header data
     * @throws ParseException on parse error
     */
    protected final NitfDataExtensionSegmentHeader readDataExtensionSegmentHeader(final NitfReader reader, final int i) throws ParseException {
        NitfDataExtensionSegmentHeaderParser dataExtensionSegmentHeaderParser = new NitfDataExtensionSegmentHeaderParser();
        NitfDataExtensionSegmentHeader dataExtensionSegmentHeader = dataExtensionSegmentHeaderParser.parse(reader);
        dataExtensionSegmentHeader.setDataExtensionSegmentDataLength(nitfFileLevelHeader.getDataExtensionSegmentDataLengths().get(i));
        return dataExtensionSegmentHeader;
    }

    /**
     * Read the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment header before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegmentHeader the header for the data extension segment that is to be read
     * @param reader the reader to use to read the data.
     * @throws ParseException on failure.
     */
    protected final void readDataExtensionSegmentData(final NitfDataExtensionSegmentHeader dataExtensionSegmentHeader,
            final NitfReader reader) throws ParseException {
        if (dataExtensionSegmentHeader.getDataExtensionSegmentDataLength() > 0) {
            if (dataExtensionSegmentHeader.isTreOverflow(reader.getFileType())) {
                initialiseTreCollectionParserIfRequired();
                TreCollectionImpl overflowTres = treCollectionParser.parse(reader, dataExtensionSegmentHeader.getDataExtensionSegmentDataLength());
                dataExtensionSegmentHeader.mergeTREs(overflowTres);
            } else if (!"STREAMING_FILE_HEADER".equals(dataExtensionSegmentHeader.getIdentifier().trim())) {
                dataExtensionSegmentData.add(reader.readBytesRaw(dataExtensionSegmentHeader.getDataExtensionSegmentDataLength()));
            }
        }
    }

    /**
     * Skip the data extension segment data.
     *
     * The reader is assumed to be positioned at the end of the segment before this call, and will be positioned
     * at the start of the next header after this call.
     *
     * @param dataExtensionSegmentHeader the header for the data extension segment that is to be skipped
     * @param reader the reader to use to skip the data.
     * @throws ParseException on failure.
     */
    protected final void skipDataExtensionSegmentData(final NitfDataExtensionSegmentHeader dataExtensionSegmentHeader,
            final NitfReader reader) throws ParseException {
        if (dataExtensionSegmentHeader.getDataExtensionSegmentDataLength() > 0) {
            reader.skip(dataExtensionSegmentHeader.getDataExtensionSegmentDataLength());
        }
    }
//</editor-fold>

    /**
     * Return the image segment headers associated with this file.
     *
     * @return image segment headers
     */
    public final List<NitfImageSegmentHeader> getImageSegmentHeaders() {
        return imageSegmentHeaders;
    }

    /**
     * Return the image segment headers associated with this file.
     *
     * @return image segment headers
     */
    public final List<NitfGraphicSegmentHeader> getGraphicSegmentHeaders() {
        return graphicSegmentHeaders;
    }

    /**
     * Return the symbol segment headers associated with this file.
     *
     * @return symbol segment headers
     */
    public final List<NitfSymbolSegmentHeader> getSymbolSegmentHeaders() {
        return symbolSegmentHeaders;
    }

    /**
     * Return the label segment headers associated with this file.
     *
     * @return label segment headers
     */
    public final List<NitfLabelSegmentHeader> getLabelSegmentHeaders() {
        return labelSegmentHeaders;
    }

    /**
     * Return the text segments associated with this file.
     *
     * @return text segments
     */
    public final List<NitfTextSegmentHeader> getTextSegmentHeaders() {
        return textSegmentHeaders;
    }

    /**
     * Return the data extension segment headers associated with this file.
     *
     * @return data extension segment headers
     */
    public final List<NitfDataExtensionSegmentHeader> getDataExtensionSegmentHeaders() {
         return dataExtensionSegmentHeaders;
    }

    /**
     * Handle the image segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleImageSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the symbol segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleSymbolSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the label segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleLabelSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the image segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleGraphicSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the text segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleTextSegment(final NitfReader reader, final int i) throws ParseException;

    /**
     * Handle the data extension segment header and data.
     *
     * @param reader the reader to use, assumed to be positioned at the start of the header
     * @param i the index (zero base) of the segment to read
     * @throws ParseException if there is a problem handling the segment
     */
    protected abstract void handleDataExtensionSegment(final NitfReader reader, final int i) throws ParseException;

    final void registerAdditionalTREdescriptor(final Source source) throws ParseException {
        initialiseTreCollectionParserIfRequired();
        treCollectionParser.registerAdditionalTREdescriptor(source);
    }

    @Override
    public final TreCollectionImpl parseTREs(final NitfReader reader, final int length) throws ParseException {
        initialiseTreCollectionParserIfRequired();
        return treCollectionParser.parse(reader, length);
    }
}
