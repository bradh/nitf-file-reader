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
package org.codice.imaging.nitf.render.imagerep;

import java.util.HashMap;
import java.util.Map;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.NitfImageBand;
import org.codice.imaging.nitf.core.image.PixelJustification;

public class ImageRepresentationHandlerFactory {

    private static final int NOT_VISIBLE_MAPPED = -1;
    private static final int BAND_NOT_FOUND = -2;

    public static ImageRepresentationHandler forImageSegment(ImageSegment segment) {

        switch (segment.getImageRepresentation()) {
            case MONOCHROME: {
                return getMonoImageRepresentationHandler(segment, 0);
            }
            case RGBTRUECOLOUR: {
                return getRgbImageRepresentationHandler(segment);
            }
            case MULTIBAND: {
                return getHandlerForMultiband(segment);
            }
            case RGBLUT: {
                return getRgbLUTImageRepresentationHandler(segment);
            }
            //add other (more complex) cases here
            default:
                return null;
        }
    }

    private static ImageRepresentationHandler getRgbImageRepresentationHandler(ImageSegment segment) {
        if (segment.getNumberOfBitsPerPixelPerBand() != 8) {
            // It can be 8, 16 or 32 once we are at CLEVEL 6, but so far we can only do 8 (enough for CLEVEL 3 and 5)
            // TODO: implement 16 bit support [IMG-112]
            // TODO: implement 32 bit support [IMG-113]
            return null;
        }
        Map<Integer, Integer> bandMapping = getRgb24ImageRepresentationMapping(segment);
        return new Rgb24ImageRepresentationHandler(bandMapping);
    }

    private static Map<Integer, Integer> getRgb24ImageRepresentationMapping(final ImageSegment imageSegment) {
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int bandIndex = 0; bandIndex < imageSegment.getNumBands(); bandIndex++) {
            int leftShift;
            switch (imageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
                case "R":
                    leftShift = 2 * Byte.SIZE;
                    break;
                case "G":
                    leftShift = Byte.SIZE;
                    break;
                case "B":
                    leftShift = 0;
                    break;
                default:
                    leftShift = NOT_VISIBLE_MAPPED;
            }
            mapping.put(bandIndex, leftShift);
        }
        return mapping;
    }

    private static ImageRepresentationHandler getHandlerForMultiband(final ImageSegment segment) {
        if (irepbandsHasRgb(segment)) {
            return getRgbImageRepresentationHandler(segment);
        }
        int firstMonoBandZeroBase = getFirstMonoBandZeroBase(segment);
        if (firstMonoBandZeroBase != BAND_NOT_FOUND) {
            return getMonoImageRepresentationHandler(segment, firstMonoBandZeroBase);
        }
        // TODO: LU case
        // TODO: no representation
        return null;
    }

    private static boolean irepbandsHasRgb(ImageSegment segment) {
        boolean hasR = false;
        boolean hasG = false;
        boolean hasB = false;
        for (int i = 0; i < segment.getNumBands(); i++) {
            NitfImageBand band = segment.getImageBandZeroBase(i);
            if (null != band.getImageRepresentation()) {
                switch (band.getImageRepresentation()) {
                    case "R":
                        hasR = true;
                        break;
                    case "G":
                        hasG = true;
                        break;
                    case "B":
                        hasB = true;
                        break;
                }
            }
        }
        return (hasR && hasG && hasB);
    }

    private static int getFirstMonoBandZeroBase(ImageSegment segment) {
        for (int bandIndex = 0; bandIndex < segment.getNumBands(); bandIndex++) {
            NitfImageBand band = segment.getImageBandZeroBase(bandIndex);
            if (null != band.getImageRepresentation() && ("M".equals(band.getImageRepresentation()))) {
                return bandIndex;
            }
        }
        return BAND_NOT_FOUND;
    }

    private static ImageRepresentationHandler getMonoImageRepresentationHandler(ImageSegment segment, int selectedBandZeroBase) {
        switch (segment.getPixelValueType()) {
            case BILEVEL:
                return getMonoBilevelImageRepresentationHandler(segment, selectedBandZeroBase);
            case INTEGER:
                return getMonoIntegerImageRepresentationHandler(segment, selectedBandZeroBase);
            case SIGNEDINTEGER:
                // TODO: [IMG-108] implement this
                return null;
            case REAL:
                // TODO: [IMG-107] implement this
                return null;
            case COMPLEX:
                // TODO: [IMG-109] implement this
                return null;
            default:
                throw new UnsupportedOperationException("Unsupported pixel value type:" + segment.getPixelValueType().getTextEquivalent());
        }
    }

    private static ImageRepresentationHandler getMonoBilevelImageRepresentationHandler(ImageSegment segment, int selectedBandZeroBase) {
        if (segment.getNumberOfBitsPerPixelPerBand() != 1) {
            throw new UnsupportedOperationException("Pixel Value of bilevel (B) must be 1 bit per pixel (NBPP = 1)");
        }
        return new Mono1ImageRepresentationHandler(selectedBandZeroBase);
    }

    private static ImageRepresentationHandler getMonoIntegerImageRepresentationHandler(ImageSegment segment, int selectedBandZeroBase) {
        if (segment.getPixelJustification().equals(PixelJustification.LEFT) || (segment.getActualBitsPerPixelPerBand() == segment.getNumberOfBitsPerPixelPerBand())) {
            switch (segment.getNumberOfBitsPerPixelPerBand()) {
                case 8:
                    return new Mono8IntegerImageRepresentationHandler(selectedBandZeroBase);
                case 12:
                    return new Mono16BitshiftIntegerImageRepresentationHandler(segment, selectedBandZeroBase);
                case 16:
                    return new Mono16IntegerImageRepresentationHandler(selectedBandZeroBase);
                // TODO: add 32 [IMG-110] and 64 [IMG-111] NBPP cases
                default:
                    return null;
            }
        } else if (segment.getNumberOfBitsPerPixelPerBand() == 8) {
            return new Mono8BitshiftIntegerImageRepresentationHandler(segment, selectedBandZeroBase);
        } else if (segment.getNumberOfBitsPerPixelPerBand() <= 16) {
            return new Mono16BitshiftIntegerImageRepresentationHandler(segment, selectedBandZeroBase);
        } else {
            // TODO: add 32 [IMG-110] and 64 [IMG-111] NBPP cases
            return null;
        }
    }

    private static ImageRepresentationHandler getRgbLUTImageRepresentationHandler(ImageSegment segment) {
        if (segment.getNumberOfBitsPerPixelPerBand() == 1) {
            return new RGBLUT1ImageRepresentationHandler(segment);
        } else if (segment.getNumberOfBitsPerPixelPerBand() == 8) {
            return new RGBLUT8ImageRepresentationHandler(segment);
        } else {
            return null;
        }
    }
}
