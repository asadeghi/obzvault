/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author asadeghi
 */
public class VaultFileFilter extends javax.swing.filechooser.FileFilter {

    /**
     * Creates a file filter that accepts only *.vault files.
     *
     */
    public VaultFileFilter() {
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * accepts directories and .vault files.
     *
     */
    public boolean accept(File f) {
        boolean fAccept;

        fAccept = false;
        if (f != null) {
            if (f.isDirectory()) {
                fAccept = true;
            } else {
                String name = f.getName().toLowerCase();
                fAccept = name.endsWith(".vault");
            }
        }
        
        return fAccept;
    }

    /**
     * Returns the human readable description of this filter.
     */
    public String getDescription() {
        return "*.vault Encrypted Text";
    }
}
