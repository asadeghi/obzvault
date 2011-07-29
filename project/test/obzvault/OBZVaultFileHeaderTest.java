/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class OBZVaultFileHeaderTest {

    private File _fileT;
    private OBZVaultFileHeader _hdrTest;

    public OBZVaultFileHeaderTest() {
    }

    @Before
    public void setUp() throws Exception {
        _fileT = File.createTempFile("obzvaultexisting", ".vault");
        _fileT.deleteOnExit();

        _hdrTest = new OBZVaultFileHeader();
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        
        _hdrTest.setFileType(OBZVaultFileHeader.FT_3DES_SHA1);
        _hdrTest.setVersion("A happy version");
        
        FileOutputStream fos = new FileOutputStream(_fileT);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(_hdrTest);
        out.close();
        
        FileInputStream fis = new FileInputStream(_fileT);
        ObjectInputStream in = new ObjectInputStream(fis);
        OBZVaultFileHeader hdrT = (OBZVaultFileHeader) in.readObject();
        in.close();

        assertEquals(_hdrTest.getFileType(), hdrT.getFileType());
        assertEquals(_hdrTest.getVersion(), hdrT.getVersion());
    }
}
