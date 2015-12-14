/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.cgm;

import static org.junit.Assert.assertNotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;

import org.codice.imaging.nitf.common.reader.NitfReader;
import org.codice.imaging.nitf.graphic.NitfGraphicSegmentHeader;
import org.codice.imaging.nitf.parser.NitfFileParser;
import org.codice.imaging.nitf.parser.NitfInputStreamReader;
import org.codice.imaging.nitf.parser.strategy.AllDataExtractionParseStrategy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class CgmParserTest {

    public CgmParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void I_3051E() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3051e.ntf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void NS3051V() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "ns3051v.nsf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void I_3052A() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3052a.ntf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void I_3060A() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3060a.ntf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void I_3063F() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3063f.ntf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void I_3068A() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3068a.ntf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void NS3059A() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "ns3059a.nsf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void NS3073A() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "ns3073a.nsf";
        testOneImage(parentDirectory, testfile);
    }

    @Test
    public void NS3101B() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "ns3101b.nsf";
        testOneImage(parentDirectory, testfile);
    }

    private void testOneImage(String parentDirectory, String testfile) throws IOException {
        String inputFileName = "/" + parentDirectory + "/" + testfile;
        System.out.println("================================== Testing :" + inputFileName);
        assertNotNull("Test file missing: " + inputFileName, getClass().getResource(inputFileName));
        try {
            System.out.println("loading from InputStream");
            NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getClass().getResourceAsStream(inputFileName)));
            AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
            NitfFileParser.parse(reader, parseStrategy);

            if (parseStrategy.getGraphicSegmentHeaders().isEmpty()) {
                System.out.println("Loaded file, but found no graphic segments.");
                System.exit(0);
            }
            NitfGraphicSegmentHeader segment = parseStrategy.getGraphicSegmentHeaders().get(0);
            CgmParser parser = new CgmParser(parseStrategy.getGraphicSegmentData().get(0));
            parser.buildCommandList();
            // parser.dump();

            // System.out.println("CCS position:" + segment.getGraphicLocationColumn() + ", " + segment.getGraphicLocationRow());
            // System.out.println("BBox1:" + segment.getBoundingBox1Column() + ", " + segment.getBoundingBox1Row());
            // System.out.println("BBox2:" + segment.getBoundingBox2Column() + ", " + segment.getBoundingBox2Row());
            BufferedImage targetImage = new BufferedImage(segment.getBoundingBox2Column(), segment.getBoundingBox2Row(), BufferedImage.TYPE_INT_ARGB);
            CgmRenderer renderer = new CgmRenderer();
            renderer.setTargetImageGraphics((Graphics2D) targetImage.getGraphics(), segment.getBoundingBox2Column(), segment.getBoundingBox2Row());
            renderer.render(parser.getCommandList());
            File targetFile = new File(testfile + "cgm.png");
            ImageIO.write(targetImage, "png", targetFile);
        } catch (ParseException e) {
            System.out.println("Failed to load from InputStream " + e.getMessage());
            e.printStackTrace();
        }
    }
}
