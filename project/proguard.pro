-injars dist/obzvault.jar
-outjars build/obfuscated/obzvault.jar

-libraryjars /usr/lib/jvm/java-6-sun/jre/lib/rt.jar
-libraryjars /usr/lib/jvm/java-6-sun/jre/lib/jce.jar
-libraryjars dist/lib/AbsoluteLayout.jar
-libraryjars dist/lib/appframework-1.0.3.jar
-libraryjars dist/lib/swing-layout-1.0.3.jar
-libraryjars dist/lib/swing-worker-1.1.jar
-libraryjars dist/lib/ui.jar

-optimizationpasses 2
-printmapping
-overloadaggressively
-keepattributes Exceptions,InnerClasses,Signature,LocalVariable*Table,Deprecated,*Annotation*,Synthetic,EnclosingMethod
-adaptresourcefilenames **.properties
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF,META-INF/OFFBYZER.SF,META-INF/OFFBYZER.RSA
-verbose
-dontskipnonpubliclibraryclasses


-keep class * extends org.jdesktop.application.FrameView {
    <methods>;
}

-keep class * extends javax.swing.JDialog {
    <methods>;
}

-keep,allowshrinking class obzvault.OBZVaultFileHeader {
    <fields>;
    <methods>;
}

# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
-keep class * extends javax.swing.plaf.ComponentUI {
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
}

# Remove debugging - Throwable_printStackTrace calls. Remove all invocations of
# Throwable.printStackTrace().
-assumenosideeffects public class java.lang.Throwable {
    public void printStackTrace();
}

# Remove debugging - Thread_dumpStack calls. Remove all invocations of
# Thread.dumpStack().
-assumenosideeffects public class java.lang.Thread {
    public static void dumpStack();
}

# Remove debugging - All logging API calls. Remove all invocations of the
# logging API whose return values are not used.
-assumenosideeffects public class java.util.logging.* {
    <methods>;
}

# Remove debugging - All Log4j API calls. Remove all invocations of the
# Log4j API whose return values are not used.
-assumenosideeffects public class org.apache.log4j.** {
    <methods>;
}
