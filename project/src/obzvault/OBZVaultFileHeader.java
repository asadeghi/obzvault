/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.io.Serializable;

/**
 *
 * @author duncan
 */
public class OBZVaultFileHeader implements Serializable {

    // Keep this value constant to allow for correct versioning.
    static final long serialVersionUID = 1L;

    // Will be serialized.
    private String _strVersion;
    private Integer _nFileType;

    // Won't be serialized.
    transient public static final int FT_3DES_SHA1 = 1;
    transient public static byte[] MAGIC_NUMBER_BLOCK = new byte[]{0x00, 0x42, 0x5a, 0x56, 0x61, 0x75, 0x6c, 0x74}; // "0BZVault"
    transient public static byte[] RESERVED_BLOCK = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public String getVersion() {
        return _strVersion;
    }

    public void setVersion(String value) {
        _strVersion = value;
    }

    public Integer getFileType() {
        return _nFileType;
    }

    public void setFileType(Integer value) {
        _nFileType = value;
    }
}
