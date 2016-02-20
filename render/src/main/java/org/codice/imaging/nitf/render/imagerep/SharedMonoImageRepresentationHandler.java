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

import java.awt.image.DataBuffer;
import org.codice.imaging.nitf.render.ImageMask;

/**
 * Shared implementation details for the MONO image handler implementations.
 */
abstract class SharedMonoImageRepresentationHandler implements ImageRepresentationHandler {
    protected final int selectedBandZeroBase;

    public SharedMonoImageRepresentationHandler(final int selectedBandZeroBase) {
        this.selectedBandZeroBase = selectedBandZeroBase;
    }

    @Override
    public void renderPadPixel(ImageMask imageMask, DataBuffer data, int pixel) {
        if (imageMask.isPadPixel(data.getElem(pixel))) {
            data.setElem(pixel, 0x00);
        }
    }

}
