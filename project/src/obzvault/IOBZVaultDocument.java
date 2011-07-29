/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import java.beans.*;
import java.io.File;

/**
 *
 * @author duncan
 */
public interface IOBZVaultDocument {
    String getText();
    void setText(String strValue);

    void setAppVersion(String appVersion);

    String getTitle();
    File getFile();
    Boolean getIsDirty();
    String getLastFind();
    Boolean getIsSaved();
    Float getTrialStatus();

    Boolean saveVaultDoc();
    Boolean saveAsVaultDoc(File file, char[] rgchPwEncrypt);
    Boolean openVaultDoc(File file, char[] rgchPwDecrypt);
    Boolean openTextDoc(File file);
    
    void reset();
    int find(String strSnippet, boolean fMatchCase);
    int findNext(int iPosCaret);

    void addPropertyChangeListener(PropertyChangeListener lst);
    void removePropertyChangeListener(PropertyChangeListener lst);
}
