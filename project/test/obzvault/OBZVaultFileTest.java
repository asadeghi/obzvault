package obzvault;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class OBZVaultFileTest {

    private File _fileText;
    private String _strText;
    private char[] _rgchPwTest;
    private File _fileExisting;
    private String _strDecryptedExisting;

    @Before
    public void setUp() throws Exception {
        _rgchPwTest = new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
        
        _fileExisting = File.createTempFile("obzvaultexisting", ".vault");
        _strDecryptedExisting = "existing";
        OBZVaultDocument doc = new OBZVaultDocument();
        doc.setAppVersion("X.Y.DEV");
        doc.setText(_strDecryptedExisting);
        doc.saveAsVaultDoc(_fileExisting, _rgchPwTest);

        _fileText = File.createTempFile("obzvaulttext", ".txt");
        _strText = "Hello world!";
        OutputStream os = new FileOutputStream(_fileText);
        os.write(_strText.getBytes());
        os.close();
    }

    @After
    public void tearDown() {
        for (File file : new File[] { _fileExisting, _fileText }) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    public void testIsOBZVaultFileReturnsTrueWhenOBZVaultFile() throws Exception {
        assertTrue(OBZVaultFile.isOBZVaultFile(_fileExisting));
    }

    @Test
    public void testIsOBZVaultFileReturnsFalseWhenTextFile() throws Exception {
        assertFalse(OBZVaultFile.isOBZVaultFile(_fileText));
    }

    @Test
    public void testIsOBZVaultFileReturnsFalseWhenBadFile() throws Exception {
        assertFalse(OBZVaultFile.isOBZVaultFile(new File("wazongas!")));
    }

    @Test
    public void testGetDocumentLengthReturnsZeroWhenBadFile() throws Exception {
        assertEquals(0, OBZVaultFile.getEncryptedDocLength(new File("wazongas!!!")));
    }

    @Test
    public void testGetDocumentLengthReturnsSize() throws Exception {
        assertEquals(16, OBZVaultFile.getEncryptedDocLength(_fileExisting));
    }
}
