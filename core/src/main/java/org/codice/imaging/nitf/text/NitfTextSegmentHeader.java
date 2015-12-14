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
package org.codice.imaging.nitf.text;

import org.codice.imaging.nitf.common.segment.AbstractNitfSubSegment;
import org.codice.imaging.nitf.common.NitfDateTime;

/**
    Text segment subheader information.
*/
public class NitfTextSegmentHeader extends AbstractNitfSubSegment {

    private NitfDateTime textDateTime = null;
    private String textTitle = null;
    private TextFormat textFormat = TextFormat.UNKNOWN;
    private int textSegmentDataLength = 0;

    /**
        Default constructor.
    */
    public NitfTextSegmentHeader() {
    }

    /**
        Set text date and time.
        <p>
        This field shall contain the time (UTC) (Zulu) of origination of the text.

        @param dateTime the date and time of the text.
    */
    public final void setTextDateTime(final NitfDateTime dateTime) {
        textDateTime = dateTime;
    }

    /**
        Return text date and time.
        <p>
        This field shall contain the time (UTC) (Zulu) of origination of the text.

        @return the date and time of the text.
    */
    public final NitfDateTime getTextDateTime() {
        return textDateTime;
    }

    /**
        Set text title.
        <p>
        This field shall contain the title of the text item.

        @param title text title (80 characters maximum).
    */
    public final void setTextTitle(final String title) {
        textTitle = title;
    }

    /**
        Return text title.
        <p>
        This field shall contain the title of the text item.

        @return text title
    */
    public final String getTextTitle() {
        return textTitle;
    }

    /**
        Set the text format indicator.
        <p>
        See TextFormat for the meaning of the enumerated values.

        @param format the text format
    */
    public final void setTextFormat(final TextFormat format) {
        textFormat = format;
    }

    /**
        Return the text format indicator.
        <p>
        See TextFormat for the meaning of the enumerated values.

        @return the text format
    */
    public final TextFormat getTextFormat() {
        return textFormat;
    }

    /**
        Set the text segment data length.
        <p>
        This is the length of the contents of the associated data segment.

        @param length the text data segment length, in bytes
    */
    public final void setTextSegmentDataLength(final int length) {
        textSegmentDataLength = length;
    }

    /**
        Return the text data length.

        @return the text data segment length, in bytes
    */
    public final int getTextDataLength() {
        return textSegmentDataLength;
    }
}
