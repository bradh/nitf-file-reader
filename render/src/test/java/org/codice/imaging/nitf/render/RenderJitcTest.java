/*
 * Copyright (C) 2014 Codice Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.codice.imaging.nitf.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import junit.framework.TestCase;
import org.codice.imaging.nitf.render.flow.NitfParserInputFlow;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for JITC NITF 2.0 and NITF 2.1 test cases.
 * See:
 * http://www.gwg.nga.mil/ntb/baseline/software/testfile/Nitfv2_0/scen_2_0.html and
 * http://www.gwg.nga.mil/ntb/baseline/software/testfile/Nitfv2_1/scen_2_1.html
 */
public class RenderJitcTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderJitcTest.class);

    public RenderJitcTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testNS3038A() throws IOException, ParseException {
        testOneFile("ns3038a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3041A() throws IOException, ParseException {
        testOneFile("i_3041a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3050A() throws IOException, ParseException {
        testOneFile("ns3050a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testU_1036A() throws IOException, ParseException {
        testOneFile("U_1036A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1050A() throws IOException, ParseException {
        testOneFile("U_1050A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4003B() throws IOException, ParseException {
        testOneFile("U_4003B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4004B() throws IOException, ParseException {
        testOneFile("U_4004B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1001A() throws IOException, ParseException {
        testOneFile("U_1001A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1034A() throws IOException, ParseException {
        testOneFile("U_1034A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1101A() throws IOException, ParseException {
        testOneFile("U_1101A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1122A() throws IOException, ParseException {
        testOneFile("U_1122A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_2001A() throws IOException, ParseException {
        testOneFile("U_2001A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3002A() throws IOException, ParseException {
        testOneFile("U_3002A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3010A() throws IOException, ParseException {
        testOneFile("U_3010A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3050B() throws IOException, ParseException {
        testOneFile("U_3050B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4002A() throws IOException, ParseException {
        testOneFile("U_4002A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4005A() throws IOException, ParseException {
        testOneFile("U_4005A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4007A() throws IOException, ParseException {
        testOneFile("U_4007A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testI_3001A() throws IOException, ParseException {
        testOneFile("i_3001a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3004G() throws IOException, ParseException {
        testOneFile("i_3004g.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3201C() throws IOException, ParseException {
        testOneFile("i_3201c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3034C() throws IOException, ParseException {
        testOneFile("i_3034c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3034F() throws IOException, ParseException {
        testOneFile("i_3034f.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3228B() throws IOException, ParseException {
        testOneFile("ns3228b.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3228C() throws IOException, ParseException {
        testOneFile("i_3228c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3228D() throws IOException, ParseException {
        testOneFile("ns3228d.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3228E() throws IOException, ParseException {
        testOneFile("i_3228e.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301B() throws IOException, ParseException {
        testOneFile("ns3301b.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301E() throws IOException, ParseException {
        testOneFile("ns3301e.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3301H() throws IOException, ParseException {
        testOneFile("i_3301h.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301J() throws IOException, ParseException {
        testOneFile("ns3301j.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3301K() throws IOException, ParseException {
        testOneFile("i_3301k.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3302A() throws IOException, ParseException {
        testOneFile("ns3302a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3303A() throws IOException, ParseException {
        testOneFile("i_3303a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3310A() throws IOException, ParseException {
        testOneFile("ns3310a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3405A() throws IOException, ParseException {
        testOneFile("i_3405a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3430A() throws IOException, ParseException {
        testOneFile("i_3430a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3004F() throws IOException, ParseException {
        testOneFile("ns3004f.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3034D() throws IOException, ParseException {
        testOneFile("ns3034d.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3201A() throws IOException, ParseException {
        testOneFile("ns3201a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testV_3301F() throws IOException, ParseException {
        testOneFile("v_3301f.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testU_3058B() throws IOException, ParseException {
        testOneFile("U_3058B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testNS3361C() throws IOException, ParseException {
        testOneFile("ns3361c.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3025b() throws IOException, ParseException {
        testOneFile("i_3025b.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3309a() throws IOException, ParseException {
        testOneFile("i_3309a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3113g() throws IOException, ParseException {
        testOneFile("i_3113g.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3228b() throws IOException, ParseException {
        testOneFile("ns3228b.nsf", "JitcNitf21Samples");
    }

    private void testOneFile(final String testfile, final String parentDirectory)
            throws IOException, ParseException {
        String inputFileName = File.separator + parentDirectory + File.separator + testfile;
        LOGGER.info("================================== Testing :" + inputFileName);
        assertNotNull("Test file missing: " + inputFileName, getClass().getResource(inputFileName));

        final ThreadLocal<Integer> i = new ThreadLocal<Integer>();
        i.set(0);

        new NitfParserInputFlow()
                .inputStream(getClass().getResourceAsStream(inputFileName))
                .imageData()
                .forEachImage((header) -> {
                    NitfRenderer renderer = new NitfRenderer();

            try {
                ImageInputStream imageData = header.getData();
                        BufferedImage img = renderer.render(header);
                        ImageIO.write(img, "png", new File("target" + File.separator + testfile + "_" + i.get() + ".png"));
                    } catch (IOException|UnsupportedOperationException e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                    i.set(i.get() + 1);
                });
    }
}
