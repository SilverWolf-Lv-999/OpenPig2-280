package kakiku.pig2mod.xform;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class MyKaizo {
   private static String gDec = MyCheck.C.dec("");
   private static final String gAddFileName = "temp1";
   private static final String gAddFilePlace = "assets/pig2mod/json/";
   private static final String gAddFilePath;

   public static void check() {
      if (!check2()) {
         System.exit(0);
      }

   }

   private static boolean check2() {
      try {
         File jarFile = new File(fixURL(MyKaizo.class.getProtectionDomain().getCodeSource().getLocation()));
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         JarFile jf = new JarFile(jarFile);

         boolean var24;
         label116: {
            String hashInFile;
            try {
               JarEntry hashEntry = jf.getJarEntry(gAddFilePath);
               if (hashEntry == null) {
                  var24 = false;
                  break label116;
               }

               InputStream in = jf.getInputStream(hashEntry);

               try {
                  hashInFile = (new String(in.readAllBytes(), StandardCharsets.UTF_8)).trim();
               } catch (Throwable var15) {
                  if (in != null) {
                     try {
                        in.close();
                     } catch (Throwable var14) {
                        var15.addSuppressed(var14);
                     }
                  }

                  throw var15;
               }

               if (in != null) {
                  in.close();
               }

               byte[] buffer = new byte[8192];
               List<JarEntry> entries = Collections.list(jf.entries());
               entries.sort(Comparator.comparing(ZipEntry::getName));

               for(JarEntry entry : entries) {
                  if (!entry.isDirectory() && !entry.getName().equals(gAddFilePath) && !entry.getName().startsWith("aiueokakikukeko")) {
                     md.update(entry.getName().getBytes(StandardCharsets.UTF_8));
                     InputStream in = jf.getInputStream(entry);

                     int len;
                     try {
                        while((len = in.read(buffer)) > 0) {
                           md.update(buffer, 0, len);
                        }
                     } catch (Throwable var16) {
                        if (in != null) {
                           try {
                              in.close();
                           } catch (Throwable var13) {
                              var16.addSuppressed(var13);
                           }
                        }

                        throw var16;
                     }

                     if (in != null) {
                        in.close();
                     }
                  }
               }
            } catch (Throwable var17) {
               try {
                  jf.close();
               } catch (Throwable var12) {
                  var17.addSuppressed(var12);
               }

               throw var17;
            }

            jf.close();
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder();

            for(byte b : hash) {
               sb.append(String.format("%02x", b));
            }

            String calculatedHash = sb.toString();
            boolean ok = calculatedHash.equals(hashInFile);
            if (!ok) {
            }

            return ok;
         }

         jf.close();
         return var24;
      } catch (Exception var18) {
         return false;
      }
   }

   private static String fixURL(URL pUrl) {
      String fixedURL = pUrl.toString();
      if (fixedURL.startsWith("union:/")) {
         fixedURL = fixedURL.substring("union:/".length());
      }

      int excl = fixedURL.indexOf("!/");
      if (excl >= 0) {
         fixedURL = fixedURL.substring(0, excl);
      }

      int sharp = fixedURL.indexOf("%23");
      if (sharp >= 0) {
         fixedURL = fixedURL.substring(0, sharp);
      }

      return fixedURL;
   }

   static {
      gAddFilePath = gAddFilePlace + gAddFileName;
   }
}

