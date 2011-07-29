/*
 * Copyright (c) 2009 OFFBYZERO PTY. LTD.
 */
package obzvault;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class OBZVaultApp extends SingleFrameApplication {

    private static String[] _rgstrArgs;


    static {
        if (System.getProperty("os.name").contains("Mac OS X")) {
            System.setProperty("com.apple.macos.use-file-dialog-packages", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "OBZVault");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        OBZVaultView view = new OBZVaultView(this, _rgstrArgs);
        show(view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of OBZVaultApp
     */
    public static OBZVaultApp getApplication() {
        return Application.getInstance(OBZVaultApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        _rgstrArgs = args;
        launch(OBZVaultApp.class, args);
    }
}
