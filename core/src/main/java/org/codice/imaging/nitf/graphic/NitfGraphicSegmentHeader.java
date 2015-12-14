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
package org.codice.imaging.nitf.graphic;

import org.codice.imaging.nitf.common.segment.AbstractNitfSubSegment;

/**
    Graphic segment subheader information (NITF 2.1 / NSIF 1.0 only).
*/
public class NitfGraphicSegmentHeader extends AbstractNitfSubSegment {

    private String graphicName = null;
    private int graphicDisplayLevel = 0;
    private int graphicLocationRow = 0;
    private int graphicLocationColumn = 0;
    private int boundingBox1Row = 0;
    private int boundingBox1Column = 0;
    private GraphicColour graphicColour = GraphicColour.UNKNOWN;
    private int boundingBox2Row = 0;
    private int boundingBox2Column = 0;
    private int graphicSegmentDataLength = 0;

    /**
        Default constructor.
    */
    public NitfGraphicSegmentHeader() {
    }

    /**
        Set the image name (SNAME).
        <p>
        This field shall contain an alphanumeric name for the image.

        @param name the image name, 20 characters maximum.
    */
    public final void setGraphicName(final String name) {
        graphicName = name;
    }

    /**
        Return the image name (SNAME).
        <p>
        This field shall contain an alphanumeric name for the image.

        @return the image name
    */
    public final String getGraphicName() {
        return graphicName;
    }

    /**
        Set the image display level (SDLVL).
        <p>
        From MIL-STD-2500C: "This field shall contain a valid
        value that indicates the image display level of the
        image relative to other displayed file components in a
        composite display. The valid values are 001 to 999.
        The display level of each displayable file component
        (image or image) within a file shall be unique; that is,
        each number from 001 to 999 is the display level of, at
        most, one item. The meaning of display level is
        discussed in paragraph 5.3.3. The image or image
        component in the file having the minimum display level
        shall have attachment level 0 (ALVL000) (BCS zeros
        (0x30))."
        <p>
        Note that explanation mixes display level and attachment level.

        @param displayLevel the display level (integer format).
    */
    public final void setGraphicDisplayLevel(final int displayLevel) {
        graphicDisplayLevel = displayLevel;
    }

    /**
        Return the image display level (SDLVL).
        <p>
        From MIL-STD-2500C: "This field shall contain a valid
        value that indicates the image display level of the
        image relative to other displayed file components in a
        composite display. The valid values are 001 to 999.
        The display level of each displayable file component
        (image or image) within a file shall be unique; that is,
        each number from 001 to 999 is the display level of, at
        most, one item. The meaning of display level is
        discussed in paragraph 5.3.3. The image or image
        component in the file having the minimum display level
        shall have attachment level 0 (ALVL000) (BCS zeros
        (0x30))."
        <p>
        Note that explanation mixes display level and attachment level.

        @return the display level (integer format).
    */
    public final int getGraphicDisplayLevel() {
        return graphicDisplayLevel;
    }

    /**
        Set the row part of the image location (SLOC).
        <p>
        From MIL-STD-2500C: "The graphics location is specified by
        providing the location of the image’s origin point
        relative to the position (location of the CCS, image, or
        image to which it is attached. This field shall contain
        the image location offset from the ILOC or SLOC value
        of the CCS, image, or image to which the image is
        attached or from the origin of the CCS when the image
        is unattached (SALVL000). A row and column value of
        000 indicates no offset. Positive row and column values
        indicate offsets down and to the right, while negative
        row and column values indicate offsets up and to the left."

        @param rowNumber the row number for the image location.
    */
    public final void setGraphicLocationRow(final int rowNumber) {
        graphicLocationRow = rowNumber;
    }

    /**
        Return the row part of the image location (SLOC).
        <p>
        From MIL-STD-2500C: "The graphics location is specified by
        providing the location of the image’s origin point
        relative to the position (location of the CCS, image, or
        image to which it is attached. This field shall contain
        the image location offset from the ILOC or SLOC value
        of the CCS, image, or image to which the image is
        attached or from the origin of the CCS when the image
        is unattached (SALVL000). A row and column value of
        000 indicates no offset. Positive row and column values
        indicate offsets down and to the right, while negative
        row and column values indicate offsets up and to the left."

        @return the row number for the image location.
    */
    public final int getGraphicLocationRow() {
        return graphicLocationRow;
    }

    /**
        Set the column part of the image location (SLOC).
        <p>
        From MIL-STD-2500C: "The graphics location is specified by
        providing the location of the image’s origin point
        relative to the position (location of the CCS, image, or
        image to which it is attached. This field shall contain
        the image location offset from the ILOC or SLOC value
        of the CCS, image, or image to which the image is
        attached or from the origin of the CCS when the image
        is unattached (SALVL000). A row and column value of
        000 indicates no offset. Positive row and column values
        indicate offsets down and to the right, while negative
        row and column values indicate offsets up and to the left."

        @param columnNumber the column number for the image location.
    */
    public final void setGraphicLocationColumn(final int columnNumber) {
        graphicLocationColumn = columnNumber;
    }

    /**
        Return the column part of the image location (SLOC).
        <p>
        From MIL-STD-2500C: "The graphics location is specified by
        providing the location of the image’s origin point
        relative to the position (location of the CCS, image, or
        image to which it is attached. This field shall contain
        the image location offset from the ILOC or SLOC value
        of the CCS, image, or image to which the image is
        attached or from the origin of the CCS when the image
        is unattached (SALVL000). A row and column value of
        000 indicates no offset. Positive row and column values
        indicate offsets down and to the right, while negative
        row and column values indicate offsets up and to the left."

        @return the column number for the image location.
    */
    public final int getGraphicLocationColumn() {
        return graphicLocationColumn;
    }

    /**
        Set the row part of the first bounding box location (SBND1).
        <p>
        From MIL-STD-2500C: "This field shall contain
        an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the upper left corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (value of the SALVL field is equal to BCS
        zeros (0x30)), rrrrr and ccccc represent offsets from the
        origin of the coordinate system that is common to all
        images and graphics in the file having the value of BCS
        zeros (0x30) in the SALVL field. The range for rrrrr
        and ccccc shall be -9999 to 99999."

        @param rowNumber the row number for the first bounding box location.
    */
    public final void setBoundingBox1Row(final int rowNumber) {
        boundingBox1Row = rowNumber;
    }

    /**
        Return the row part of the first bounding box location (SBND1).
        <p>
        From MIL-STD-2500C: "This field shall contain
        an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the upper left corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (value of the SALVL field is equal to BCS
        zeros (0x30)), rrrrr and ccccc represent offsets from the
        origin of the coordinate system that is common to all
        images and graphics in the file having the value of BCS
        zeros (0x30) in the SALVL field. The range for rrrrr
        and ccccc shall be -9999 to 99999."

        @return the row number for the first bounding box location.
    */
    public final int getBoundingBox1Row() {
        return boundingBox1Row;
    }

    /**
        Set the column part of the first bounding box location (SBND1).
        <p>
        From MIL-STD-2500C: "This field shall contain
        an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the upper left corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (value of the SALVL field is equal to BCS
        zeros (0x30)), rrrrr and ccccc represent offsets from the
        origin of the coordinate system that is common to all
        images and graphics in the file having the value of BCS
        zeros (0x30) in the SALVL field. The range for rrrrr
        and ccccc shall be -9999 to 99999."

        @param columnNumber the column number for the first bounding box location.
    */
    public final void setBoundingBox1Column(final int columnNumber) {
        boundingBox1Column = columnNumber;
    }

    /**
        Return the column part of the first bounding box location (SBND1).
        <p>
        From MIL-STD-2500C: "This field shall contain
        an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the upper left corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (value of the SALVL field is equal to BCS
        zeros (0x30)), rrrrr and ccccc represent offsets from the
        origin of the coordinate system that is common to all
        images and graphics in the file having the value of BCS
        zeros (0x30) in the SALVL field. The range for rrrrr
        and ccccc shall be -9999 to 99999."

        @return the column number for the first bounding box location.
    */
    public final int getBoundingBox1Column() {
        return boundingBox1Column;
    }

    /**
        Set the image colour (SCOLOR).

        @param colour the image colour.
    */
    public final void setGraphicColour(final GraphicColour colour) {
        graphicColour = colour;
    }

    /**
        Return the image colour (SCOLOR).

        @return the image colour.
    */
    public final GraphicColour getGraphicColour() {
        return graphicColour;
    }

    /**
        Set the row part of the second bounding box location (SBND2).
        <p>
        From MIL-STD-2500C: "This field shall
        contain an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the lower right corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (SALVL field value is BCS zeros(0x30)),
        rrrrr and ccccc represent offsets from the origin of the
        coordinate system that is common to all images and
        graphics in the file having the value of BCS zeros (0x30)
        in the SALVL field. The range for rrrrr and ccccc shall
        be -9999 to 99999."

        @param rowNumber the row number for the second bounding box location.
    */
    public final void setBoundingBox2Row(final int rowNumber) {
        boundingBox2Row = rowNumber;
    }

    /**
        Return the row part of the second bounding box location (SBND2).
        <p>
        From MIL-STD-2500C: "This field shall
        contain an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the lower right corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (SALVL field value is BCS zeros(0x30)),
        rrrrr and ccccc represent offsets from the origin of the
        coordinate system that is common to all images and
        graphics in the file having the value of BCS zeros (0x30)
        in the SALVL field. The range for rrrrr and ccccc shall
        be -9999 to 99999."

        @return the row number for the second bounding box location.
    */
    public final int getBoundingBox2Row() {
        return boundingBox2Row;
    }

    /**
        Set the column part of the second bounding box location (SBND2).
        <p>
        From MIL-STD-2500C: "This field shall
        contain an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the lower right corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (SALVL field value is BCS zeros(0x30)),
        rrrrr and ccccc represent offsets from the origin of the
        coordinate system that is common to all images and
        graphics in the file having the value of BCS zeros (0x30)
        in the SALVL field. The range for rrrrr and ccccc shall
        be -9999 to 99999."

        @param columnNumber the column number for the second bounding box location.
    */
    public final void setBoundingBox2Column(final int columnNumber) {
        boundingBox2Column = columnNumber;
    }

    /**
        Return the column part of the second bounding box location (SBND2).
        <p>
        From MIL-STD-2500C: "This field shall
        contain an ordered pair of integers defining a location in
        Cartesian coordinates for use with CGM graphics. It is
        the lower right corner of the bounding box for the CGM
        image. See paragraph 5.5.2.1 for a description. The
        format is rrrrrccccc, where rrrrr is the row and ccccc is
        the column offset from ILOC or SLOC value of the item
        to which the image is attached. If the image is
        unattached (SALVL field value is BCS zeros(0x30)),
        rrrrr and ccccc represent offsets from the origin of the
        coordinate system that is common to all images and
        graphics in the file having the value of BCS zeros (0x30)
        in the SALVL field. The range for rrrrr and ccccc shall
        be -9999 to 99999."

        @return the column number for the second bounding box location.
    */
    public final int getBoundingBox2Column() {
        return boundingBox2Column;
    }

    /**
        Set the image segment data length.
        <p>
        This is the length of the contents of the associated data segment.

        @param length the image data segment length, in bytes
    */
    public final void setGraphicSegmentDataLength(final int length) {
        graphicSegmentDataLength = length;
    }

    /**
        Return the image data length.

        @return the image data segment length, in bytes
    */
    public final int getGraphicDataLength() {
        return graphicSegmentDataLength;
    }
}
