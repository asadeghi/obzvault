/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import org.junit.*;
import static org.junit.Assert.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

public class OBZVaultDocumentTest {

    private IOBZVaultDocument _docTest;
    private File _fileT;
    private File _fileText;
    private String _strTitleT;
    private String _strTitleText;
    private String _strText;
    private PropertyChangeTester _pct;
    private char[] _rgchPwTest;
    private File _fileExisting;
    private String _strTitleExisting;
    private String _strDecryptedExisting;

    @Before
    public void setUp() throws Exception {
        _docTest = new OBZVaultDocument();
        _docTest.setAppVersion("X.Y.DEV");

        _fileT = File.createTempFile("obzvault", ".vault");
        _strTitleT = _fileT.getName();
        _pct = new PropertyChangeTester();
        _rgchPwTest = new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

        _docTest.reset();
        _docTest.addPropertyChangeListener(_pct);

        _fileExisting = File.createTempFile("obzvaultexisting", ".vault");
        _strTitleExisting = _fileExisting.getName();
        _strDecryptedExisting = "existing";
        OBZVaultDocument doc = new OBZVaultDocument();
        doc.setAppVersion("X.Y.DEV");
        doc.setText(_strDecryptedExisting);
        doc.saveAsVaultDoc(_fileExisting, _rgchPwTest);

        _fileText = File.createTempFile("obzvaulttext", ".txt");
        _strTitleText = _fileText.getName();
        _strText = "Hello world!";
        OutputStream os = new FileOutputStream(_fileText);
        os.write(_strText.getBytes());
        os.close();
    }

    @After
    public void tearDown() {
        for (File file : new File[]{_fileT, _fileExisting, _fileText}) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (_docTest.getFile() != null && _docTest.getFile().exists()) {
            _docTest.getFile().delete();
        }
    }

    @Test
    public void testGetTrialStatusIsFalseByDefault() {
        assertEquals(8088f, _docTest.getTrialStatus(), 0.01f);
    }

    @Test
    public void testSetTextIsPersisted() {
        _docTest.setText("foo");
        assertEquals("foo", _docTest.getText());
    }

    @Test
    public void testTextPropertyChangedOnOpenVaultDoc() throws Exception {
        _docTest.openVaultDoc(_fileExisting, _rgchPwTest);
        assertEquals("existing", _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testTextPropertyChangedOnOpenTextDoc() throws Exception {
        _docTest.openTextDoc(_fileText);
        assertEquals(_strText, _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testTextPropertyNotChangedOnSetText() throws Exception {
        _docTest.setText("bar");
        assertNull(_pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testTitlePropertyChanged() {
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        assertEquals(_strTitleT, _pct.getValue(OBZVaultDocument.PROP_TITLE));
    }

    @Test
    public void testIsDirtyPropertyChange() {
        _docTest.setText("foo");
        assertEquals(true, _pct.getValue(OBZVaultDocument.PROP_ISDIRTY));
    }

    @Test
    public void testSaveAsVaultDocCreatesEncryptedFile() throws Exception {
        _docTest.setText("foo");
        assertTrue(_docTest.saveAsVaultDoc(_fileT, _rgchPwTest));
        assertTrue(_fileT.exists());
        assertTrue(_docTest.getIsSaved());
        assertFileNotContain(_docTest.getFile(), "foo");    // doesn't contain "foo" because it's been encrypted
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsVaultDocThrowsExceptionOnNullPassword() throws Exception {
        _docTest.saveAsVaultDoc(_fileT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsVaultDocThrowsExceptionOnNullFile() throws Exception {
        _docTest.saveAsVaultDoc(null, _rgchPwTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyChangeListenerThrowsExceptionOnNullListener() throws Exception {
        _docTest.addPropertyChangeListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindThrowsExceptionOnNullToken() throws Exception {
        _docTest.find(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenTextDocThrowsExceptionOnNullFile() throws Exception {
        _docTest.openTextDoc(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovePropertyChangeListenerThrowsExceptionOnNullListener() throws Exception {
        _docTest.removePropertyChangeListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenVaultDocThrowsExceptionOnNullFile() throws Exception {
        _docTest.openVaultDoc(null, _rgchPwTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenVaultDocThrowsExceptionOnNullPassword() throws Exception {
        _docTest.openVaultDoc(_fileT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAppVersionThrowsExceptionOnNullVersion() throws Exception {
        _docTest.setAppVersion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextThrowsExceptionOnNullText() throws Exception {
        _docTest.setText(null);
    }

    @Test
    public void testOpenVaultDocReadsFile() throws Exception {
        _docTest.setText("foo");
        Boolean fResult = _docTest.openVaultDoc(_fileExisting, _rgchPwTest);

        assertTrue(fResult);
        assertTrue(_docTest.getIsSaved());
        assertEquals(false, _pct.getValue(OBZVaultDocument.PROP_ISDIRTY));
        assertEquals(_strTitleExisting, _pct.getValue(OBZVaultDocument.PROP_TITLE));
        assertEquals(_strDecryptedExisting, _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testOpenTextDocReadsTextFile() throws Exception {
        _docTest.setText("foo");
        Boolean fResult = _docTest.openTextDoc(_fileText);

        assertTrue(fResult);
        assertFalse(_docTest.getIsSaved());
        assertEquals(true, _pct.getValue(OBZVaultDocument.PROP_ISDIRTY));
        assertEquals(_strTitleText, _pct.getValue(OBZVaultDocument.PROP_TITLE));
        assertEquals(_strText, _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testOpenTextDocResetsIsSaved() throws Exception {
        _docTest.setText("foo");
        assertTrue(_docTest.saveAsVaultDoc(_fileT, _rgchPwTest));
        assertTrue(_docTest.openTextDoc(_fileText));
        assertFalse(_docTest.getIsSaved());
    }

    @Test
    public void testOpenVaultDocReturnsFalseWithWrongPassword() throws Exception {
        _docTest.setText("foo");

        OBZVaultDocument doc = new OBZVaultDocument();
        doc.reset();
        Boolean fResult = doc.openVaultDoc(_fileExisting, new char[]{'f', 'o', 'o'});

        assertFalse(fResult);
        assertFalse(doc.getIsDirty());
        assertFalse(doc.getIsSaved());
        assertEquals("", doc.getText());
        assertEquals("untitled", doc.getTitle());
    }

    @Test
    public void testResetClearsContents() throws Exception {
        _docTest.setText("foo");
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        _docTest.reset();

        assertEquals(false, _docTest.getIsDirty());
        assertEquals(false, _docTest.getIsSaved());
        assertEquals("untitled", _docTest.getTitle());
        assertEquals("", _docTest.getText());
    }

    @Test
    public void testOpenVaultDocIgnoresBadFile() throws Exception {
        Boolean fResult = _docTest.openVaultDoc(new File("//\\/\\/!!!$###@@|?|@?#@#|/&"), _rgchPwTest);

        assertFalse(fResult);
        assertFalse(_docTest.getIsSaved());
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_ISDIRTY));
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_TITLE));
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testOpenTextDocIgnoresBadFile() throws Exception {
        Boolean fResult = _docTest.openTextDoc(new File("//\\/\\/!!!$###@@|?|@?#@#|/&"));

        assertFalse(fResult);
        assertFalse(_docTest.getIsSaved());
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_ISDIRTY));
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_TITLE));
        assertEquals(null, _pct.getValue(OBZVaultDocument.PROP_TEXT));
    }

    @Test
    public void testGetTitleReturnsTitleAfterSaving() {
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        assertEquals(_strTitleT, _docTest.getTitle());
    }

    @Test
    public void testIsDirtyIsSetTrueAfterChange() {
        assertFalse(_docTest.getIsDirty());
        _docTest.setText("foo");
        assertTrue(_docTest.getIsDirty());
    }

    @Test
    public void testIsDirtyIsSetFalseAfterSaveAs() {
        _docTest.setText("foo");
        assertTrue(_docTest.getIsDirty());
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        assertFalse(_docTest.getIsDirty());
    }

    @Test
    public void testFindFindsValue() {
        _docTest.setText("foobarbazbar");
        assertEquals(3, _docTest.find("bar", true));
    }

    @Test
    public void testFindNextFindsNextValue() {
        _docTest.setText("foobarbazbar");
        int iPos = _docTest.find("bar", true);
        assertEquals(9, _docTest.findNext(iPos));
    }

    @Test
    public void testFindNextFindsOnlyNextValue() {
        _docTest.setText("foobarbazbar");
        _docTest.find("bar", true);
        assertEquals(9, _docTest.findNext(3));
        assertEquals(-1, _docTest.findNext(9));
        assertEquals(-1, _docTest.findNext(9));
    }

    @Test
    public void testFindFindsValueCaseInsensitive() {
        _docTest.setText("foobarbazbar");
        assertEquals(3, _docTest.find("BAR", false));
    }

    @Test
    public void testFindNextFindsNextValueCaseInsensitive() {
        _docTest.setText("foobarbazbar");
        _docTest.find("BAR", false);
        assertEquals(9, _docTest.findNext(3));
    }

    @Test
    public void testFindNextFindsNextValueWhenAdjacent() {
        _docTest.setText("barbar");
        _docTest.find("bar", true);
        assertEquals(3, _docTest.findNext(0));
    }

    @Test
    public void testFindSetsLastFind() {
        _docTest.find("bar", true);
        assertEquals("bar", _docTest.getLastFind());
    }

    @Test
    public void testFindDoesntFindBadValue() {
        _docTest.setText("foobarbazbar");
        assertEquals(-1, _docTest.find("boink", true));
    }

    @Test
    public void testFindNextDoesntFindBadValue() {
        _docTest.setText("foobarbazbar");
        _docTest.find("boink", true);
        assertEquals(-1, _docTest.findNext(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindThrowsExceptionWithoutFind() {
        _docTest.setText("foobarbazbar");
        assertEquals(-1, _docTest.findNext(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testSaveThrowsExceptionWithoutSaveAs() {
        _docTest.saveVaultDoc();
    }

    @Test(expected = IllegalStateException.class)
    public void testSaveThrowsExceptionIfCalledBeforeSetAppVersion() {
        OBZVaultDocument docT = new OBZVaultDocument();
        docT.saveAsVaultDoc(_fileT, _rgchPwTest);
    }

    @Test
    public void testSaveUpdatesFile() throws Exception {
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        _docTest.setText("foo");
        _docTest.saveVaultDoc();

        OBZVaultDocument doc = new OBZVaultDocument();
        assertTrue(doc.openVaultDoc(_fileT, _rgchPwTest));
        assertEquals(_docTest.getText(), doc.getText());
    }

    @Test
    public void testSaveAsSetsIsSaved() {
        assertFalse(_docTest.getIsSaved());
        _docTest.saveAsVaultDoc(_fileT, _rgchPwTest);
        assertTrue(_docTest.getIsSaved());
    }

    private void assertFileNotContain(File file, String value) throws Exception {
        if (fileContains(file, value)) {
            fail(file.getPath() + " contains '" + value + "'");
        }
    }

    private Boolean fileContains(File file, String value) throws Exception {
        Boolean fFound = false;
        BufferedReader br = null;

        br = new BufferedReader(new FileReader(file));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            if (strLine.contains(value)) {
                fFound = true;
                break;
            }
        }
        br.close();
        return fFound;
    }

    private class PropertyChangeTester implements PropertyChangeListener {

        private Hashtable<String, Object> _htChanges;

        public PropertyChangeTester() {
            _htChanges = new Hashtable<String, Object>();
        }

        public void propertyChange(PropertyChangeEvent evt) {
            _htChanges.put(evt.getPropertyName(), evt.getNewValue());
        }

        public Object getValue(String strPropertyName) {
            return _htChanges.get(strPropertyName);
        }
    }
}