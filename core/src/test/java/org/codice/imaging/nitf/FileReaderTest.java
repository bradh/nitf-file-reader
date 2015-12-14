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
package org.codice.imaging.nitf;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.codice.imaging.nitf.common.reader.FileReader;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class FileReaderTest {

    private final String testfile = "/WithBE.ntf";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testBadFilenameConstructorArgument() throws ParseException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader goodReader = new FileReader(getClass().getResource(testfile).getPath());
        assertNotNull(goodReader);

        exception.expect(ParseException.class);
        exception.expectMessage("no such file not found: no such file");
        FileReader badReader = new FileReader("no such file");
    }

    @Test
    public void testBadFileConstructorArgument() throws ParseException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader goodReader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(goodReader);

        exception.expect(ParseException.class);
        exception.expectMessage("no such file not found: no such file");
        FileReader badReader = new FileReader(new File("no such file"));
    }
}
