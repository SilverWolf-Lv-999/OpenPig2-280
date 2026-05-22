package kakiku.pig2mod.xform;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MyCheck {
   public void check1() {
      if (!(new MyCheckB()).check1()) {
         System.exit(0);
      }

   }

   public void check2() {
      if (!(new MyCheckB()).check2()) {
         System.exit(0);
      }

   }

   public static boolean check3() {
      try {
         return (Boolean)Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f97").invoke((Object)null);
      } catch (Exception var1) {
         return false;
      }
   }

   public static void MyLog4(String string) {
   }

   public static class C {
      private static final String ALGO = "AES";
      private static String KEY = "Pig2dummykeyPig2";

      public static void setTrueKey(String key) {
         KEY = key;
      }

      public static String enc(String plainText) {
         try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(1, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
         } catch (Exception var4) {
            return "";
         }
      }

      public static String dec(String cipherText) {
         try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(2, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
         } catch (Exception var4) {
            return "";
         }
      }

      static {
         try {
            C.class.getMethod("setTrueKey").invoke((Object)null, "TgfVcFEc4zqZch+n");
         } catch (Exception var1) {
         }

      }
   }
}

