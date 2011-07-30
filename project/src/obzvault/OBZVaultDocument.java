/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.beans.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

/**
 *
 * The OBZVaultDocument type persists using the following file structure:
 *
 * --------------------
 * 0x00, 0x42, 0x5a, 0x56, 0x61, 0x75, 0x6c, 0x74      "\0BZVault" (8 bytes)
 * 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00      reserved (8 bytes)
 * ...                                                 Java-serialized header
 * 0x00, 0x42, 0x5a, 0x56, 0x61, 0x75, 0x6c, 0x74      "\0BZVault" (8 bytes)
 * ...                                                 3DES-encoded byte array
 * --------------------
 *
 */
public class OBZVaultDocument implements IOBZVaultDocument, Serializable {

    public static final String PROP_TEXT = "text";
    public static final String PROP_TITLE = "title";
    public static final String PROP_ISDIRTY = "isdirty";
    public static final String CIPHER_TYPE = "PBEWithSHA1AndDESede";
    private String _strAppVersion;
    private String _strText;
    private PropertyChangeSupport _pcs;
    private String _strTitle;
    private Boolean _fIsDirty;
    private File _file;
    private String _strLastFind;
    private Boolean _fMatchCaseLastFind;
    private SecretKey _sk;

    // Change this to a value greater than 6502f to enable trial status.
    private Float _dwIsTrialFlag = 8088f;

    public OBZVaultDocument() {
        _pcs = new PropertyChangeSupport(this);
    }

    public Boolean saveVaultDoc() {
        if (_file == null) {
            throw new IllegalStateException("save() can only be called after saveAs()");
        }
        if (_strAppVersion == null) {
            throw new IllegalStateException("You must setAppVersion() before saving.");
        }

        return saveAs(_file, _sk);
    }

    public void setAppVersion(String value) {
        if (value == null) {
            throw new IllegalArgumentException("You must specify the parameter value.");
        }

        _strAppVersion = value;
    }

    public Float getTrialStatus() {
        return _dwIsTrialFlag;
    }

    public File getFile() {
        return _file;
    }

    public Boolean getIsSaved() {
        return (_file != null);
    }

    public void reset() {
        String strTextOld = _strText;
        _file = null;

        // use setters not private fields so we trigger binding
        setText("", false);
        setTitle("untitled");
        setIsDirty(false);

        _pcs.firePropertyChange(PROP_TEXT, strTextOld, _strText);
    }

    public String getText() {
        return _strText;
    }

    public String getTitle() {
        return _strTitle;
    }

    public String getLastFind() {
        return _strLastFind;
    }

    public void setText(String strValue) {
        if (strValue == null) {
            throw new IllegalArgumentException("You must specify the value parameter.");
        }
        setText(strValue, true);
    }

    public void addPropertyChangeListener(PropertyChangeListener lst) {
        if (lst == null) {
            throw new IllegalArgumentException("You must specify the parameter lst.");
        }

        _pcs.addPropertyChangeListener(lst);
    }

    public void removePropertyChangeListener(PropertyChangeListener lst) {
        if (lst == null) {
            throw new IllegalArgumentException("You must specify the parameter lst.");
        }

        _pcs.removePropertyChangeListener(lst);
    }

    public Boolean saveAsVaultDoc(File file, char[] rgchPwEncrypt) {
        Boolean fSaved;

        if (_strAppVersion == null) {
            throw new IllegalStateException("You must setAppVersion() before saving.");
        }
        if (file == null) {
            throw new IllegalArgumentException("You must specify the file parameter.");
        }
        if (rgchPwEncrypt == null) {
            throw new IllegalArgumentException("You must specify the rgchPwEncrypt parameter.");
        }

        try {
            fSaved = saveAs(file, generateSecretKey(rgchPwEncrypt));
        } catch (Exception e) {
            e.printStackTrace();
            fSaved = false;
        }

        return fSaved;
    }

    public Boolean getIsDirty() {
        return _fIsDirty;
    }

    public Boolean openTextDoc(File file) {
        Boolean fOpened;
        String strText = null;

        if (file == null) {
            throw new IllegalArgumentException("You must specify the file parameter.");
        }

        try {
            strText = getText(file);
            fOpened = true;
        } catch (Exception e) {
            e.printStackTrace();
            fOpened = false;
        }

        if (fOpened) {
            String strTextOld = _strText;
            setTitle(file.getName());
            setText(strText, false);
            setIsDirty(true);
            _file = null;
            _sk = null;
            _pcs.firePropertyChange(PROP_TEXT, strTextOld, strText);
        }

        return fOpened;
    }

    public Boolean openVaultDoc(File file, char[] rgchPwEncrypt) {
        Boolean fOpened;

        if (file == null) {
            throw new IllegalArgumentException("You must specify the file parameter.");
        }
        if (rgchPwEncrypt == null) {
            throw new IllegalArgumentException("You must specify the rgchPwEncrypt parameter.");
        }

        try {
            fOpened = open(file, generateSecretKey(rgchPwEncrypt));
            for (int irgchPwEncrypt = 0; irgchPwEncrypt < rgchPwEncrypt.length; irgchPwEncrypt++) {
                rgchPwEncrypt[irgchPwEncrypt] = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fOpened = false;
        }

        if (fOpened) {
            _file = file;
        }

        return fOpened;
    }

    public int find(String strSnippet, boolean fMatchCase) {

        String strHaystack;
        String strNeedle;

        if (strSnippet == null) {
            throw new IllegalArgumentException("You must specify the strSnippet parameter.");
        }

        strHaystack = fMatchCase ? _strText : _strText.toLowerCase();
        strNeedle = fMatchCase ? strSnippet : strSnippet.toLowerCase();

        _strLastFind = strNeedle;
        _fMatchCaseLastFind = fMatchCase;
        return strHaystack.indexOf(strNeedle);
    }

    public int findNext(int iPosCaret) {

        if (_strLastFind == null) {
            throw new IllegalStateException("findNext() can only be called after find()");
        }

        String strHaystack = _fMatchCaseLastFind ? _strText : _strText.toLowerCase();
        return strHaystack.indexOf(_strLastFind, iPosCaret + 1);
    }

    private Boolean saveAs(File file, SecretKey sk) {

        OBZVaultFileHeader hdr;
        Boolean fSaved = false;

        try {
            OBZVaultFile of = new OBZVaultFile();

            hdr = new OBZVaultFileHeader();
            hdr.setFileType(OBZVaultFileHeader.FT_3DES_SHA1);
            hdr.setVersion(_strAppVersion);

            of.setHeader(hdr);
            of.setEncryptedBlock(rgbEncryptFromStr(_strText, sk));
            of.write(file);

            fSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
            fSaved = false;
        }

        if (fSaved) {
            setTitle(file.getName());
            setIsDirty(false);
            _sk = sk;
            _file = file;
        }

        return fSaved;
    }

    // zeroes rgchpw
    private SecretKey generateSecretKey(char[] rgchPw) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(rgchPw);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CIPHER_TYPE);
        SecretKey sk = keyFactory.generateSecret(keySpec);

        return sk;
    }

    private byte[] rgbEncryptFromStr(String strSrc, SecretKey sk) throws Exception {
        Cipher cipher = getCipher(sk, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(strSrc.getBytes());
    }

    private String strDecryptFromRgb(byte[] rgbEncrypted, SecretKey sk) throws Exception {
        Cipher cipher = getCipher(sk, Cipher.DECRYPT_MODE);
        return new String(cipher.doFinal(rgbEncrypted));
    }

    private Cipher getCipher(SecretKey sk, int imode) throws Exception {
        byte[] rgbSalt = new byte[]{'a', 's', 'a', 't', 't', 'O', 'B', 'Z'};
        PBEParameterSpec paramSpec = new PBEParameterSpec(rgbSalt, 1000);
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(imode, sk, paramSpec);

        return cipher;
    }

    private Boolean open(File file, SecretKey sk) {
        Boolean fOpened = false;
        String strText = "";
        OBZVaultFile of = null;
        try {
            of = new OBZVaultFile();
            of.read(file);

            strText = strDecryptFromRgb(of.getEncryptedBlock(), sk);
            fOpened = true;
        } catch (BadPaddingException bpe) {
            fOpened = false;
        } catch (Exception e) {
            e.printStackTrace();
            fOpened = false;
        }

        if (fOpened) {
            String strTextOld = _strText;
            setTitle(file.getName());
            setText(strText, false);
            _sk = sk;
            _pcs.firePropertyChange(PROP_TEXT, strTextOld, strText);
        }

        return fOpened;
    }

    private static String getText(File file) throws IOException {

        StringBuilder sbResult = new StringBuilder();
        BufferedReader brIn = null;
        String strLine;
        Boolean fFirst;

        try {
            brIn = new BufferedReader(new FileReader(file));
            fFirst = true;

            strLine = null;

            // readLine() returns ...
            // the content of a line MINUS the newline.
            // null only for the END of the stream.
            // an empty String if two newlines appear in a row.
            while ((strLine = brIn.readLine()) != null) {
                if (!fFirst) {
                    sbResult.append(System.getProperty("line.separator"));
                }
                sbResult.append(strLine);
                fFirst = false;
            }
        } finally {
            if (brIn != null) {
                brIn.close();
            }
        }

        return sbResult.toString();
    }

    private void setIsDirty(Boolean fValue) {
        Boolean fIsDirtyOld = _fIsDirty;
        _fIsDirty = fValue;
        _pcs.firePropertyChange(PROP_ISDIRTY, fIsDirtyOld, _fIsDirty);
    }

    private void setTitle(String strValue) {
        String strTitleOld = _strTitle;
        _strTitle = strValue;
        _pcs.firePropertyChange(PROP_TITLE, strTitleOld, _strTitle);
    }

    private void setText(String strValue, Boolean fIsDirty) {
        if (!strValue.equals(_strText)) {
            _strText = strValue;
            setIsDirty(fIsDirty);
        }
    }
}

