package obzvault;

import java.io.*;

public class OBZVaultFile {

    private OBZVaultFileHeader _hdr;
    private byte[] _rgbEncBlock;

    public static Boolean isOBZVaultFile(File file) {
        OBZVaultFileHeader hdr = null;
        OBZVaultFile of;

        try {
            of = new OBZVaultFile();
            of.read(file);
            hdr = of.getHeader();
        } catch (Exception e) {
            // most likely not an OBZVault file, so just silently swallow
        }

        return (hdr != null);
    }

    /*
     * getEncryptedDocLength() returns the length of the encryped document, so will
     * always return a multiple of 8 bytes for a 3DES-encrypted file type.
     */
    public static int getEncryptedDocLength(File file) {
        OBZVaultFileHeader hdr = null;
        OBZVaultFile of;
        int cbDocument = 0;

        try {
            of = new OBZVaultFile();
            of.read(file);
            cbDocument = of.getEncryptedBlock().length;
        } catch (Exception e) {
            // most likely not an OBZVault file, so just silently swallow
        }

        return cbDocument;
    }

    public void write(File file) throws IOException {

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);

            // write magic number, reserved
            fos.write(OBZVaultFileHeader.MAGIC_NUMBER_BLOCK);
            fos.write(OBZVaultFileHeader.RESERVED_BLOCK);

            // write header
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(_hdr);

            // write magic number, 3DES block
            fos.write(OBZVaultFileHeader.MAGIC_NUMBER_BLOCK);
            fos.write(_rgbEncBlock);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    // intentionally swallow exceptions in finally{}
                    e.printStackTrace();
                }
            }
        }
    }

    public void read(File file) throws IOException, OBZVaultFileFormatException, OBZVaultFileTypeException, ClassNotFoundException {
        byte[] rgbT;
        ObjectInputStream in;
        long ncFile = 0;

        _hdr = null;
        _rgbEncBlock = null;

        InputStream is = null;
        try {
            is = new FileInputStream(file);

            ncFile = file.length();
            if (ncFile > Integer.MAX_VALUE) {
                throw new IOException("File is too large to read.");
            }

            rgbT = new byte[(int) ncFile];

            // skip the magic number and reserved bytes
            safeRead(is, rgbT, 0, OBZVaultFileHeader.MAGIC_NUMBER_BLOCK.length);
            if (!startsWithMagicNumber(rgbT)) {
                throw new OBZVaultFileFormatException();
            }
            safeRead(is, rgbT, 0, OBZVaultFileHeader.RESERVED_BLOCK.length);

            // read the OBZVaultHeader and check this is a 3DES_SHA1 file
            in = new ObjectInputStream(is);
            try {
                _hdr = (OBZVaultFileHeader) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new OBZVaultFileFormatException();
            }

            if (_hdr.getFileType() != OBZVaultFileHeader.FT_3DES_SHA1) {
                throw new OBZVaultFileTypeException(OBZVaultFileHeader.FT_3DES_SHA1, _hdr.getFileType());
            }

            // skip the magic number again
            safeRead(is, rgbT, 0, OBZVaultFileHeader.RESERVED_BLOCK.length);

            // read the actual 3DES block
            _rgbEncBlock = safeReadToEnd(is, (int) ncFile);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // intentionally swallow exceptions in finally{}
                    e.printStackTrace();
                }
            }
        }
    }

    public OBZVaultFileHeader getHeader() {
        return _hdr;
    }

    public void setHeader(OBZVaultFileHeader value) {
        _hdr = value;
    }

    public byte[] getEncryptedBlock() {
        return _rgbEncBlock;
    }

    public void setEncryptedBlock(byte[] value) {
        _rgbEncBlock = value;
    }

    private void safeRead(InputStream is, byte[] rgbBuffer, int cbInitialOffset, int cbRead) throws IOException {
        int cbBlock = 0;
        int cbOffset = cbInitialOffset;

        while ((cbInitialOffset < (cbOffset + cbRead)) &&
                (cbBlock = is.read(rgbBuffer, cbInitialOffset, cbRead - cbInitialOffset)) >= 0) {
            cbInitialOffset += cbBlock;
        }

        if (cbInitialOffset - cbOffset < cbRead) {
            throw new IOException("Could not read " + cbRead + " bytes.");
        }
    }

    private Boolean startsWithMagicNumber(byte[] rgb) {
        Boolean fResult = true;

        for (int irgbMagicNumber = 0; irgbMagicNumber < OBZVaultFileHeader.MAGIC_NUMBER_BLOCK.length; irgbMagicNumber++) {
            if (rgb[irgbMagicNumber] != OBZVaultFileHeader.MAGIC_NUMBER_BLOCK[irgbMagicNumber]) {
                fResult = false;
                break;
            }
        }

        return fResult;
    }

    private byte[] safeReadToEnd(InputStream is, int cbReadMax) throws IOException {
        int cbOffset = 0;
        int cbBlock = 0;
        byte[] rgbBufferResult;
        byte[] rgbBufferT = new byte[cbReadMax];

        while ((cbBlock = is.read(rgbBufferT, cbOffset, cbReadMax - cbOffset)) >= 0) {
            cbOffset += cbBlock;
        }

        if (is.available() > 0) {
            throw new IOException("Buffer is full, but bytes still available from stream.");
        }

        rgbBufferResult = new byte[cbOffset];
        for (int irgbBuffer = 0; irgbBuffer < cbOffset; irgbBuffer++) {
            rgbBufferResult[irgbBuffer] = rgbBufferT[irgbBuffer];
        }

        return rgbBufferResult;
    }
}
