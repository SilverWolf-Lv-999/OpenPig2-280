package kakiku.pig2mod.xform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MyNative {
   public static native int getNumber();

   public static native Object getKeisou();

   static {
      try {
         InputStream in = MyNative.class.getResourceAsStream("/assets/pig2mod/native/MyNative.dll");
         if (in == null) {
            throw new IOException("MyNative.dll not found in jar");
         }

         File temp = File.createTempFile("MyNative", ".dll");
         temp.deleteOnExit();
         Files.copy(in, temp.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
         System.load(temp.getAbsolutePath());
      } catch (Throwable var2) {
      }

   }
}

