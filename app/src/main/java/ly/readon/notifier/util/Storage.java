package ly.readon.notifier.util;


import android.content.Context;
import android.content.SharedPreferences;

import ly.readon.notifier.BuildConfig;


/**
 * @author : Bernd Verst(@berndverst)
 */
public class Storage {
    private Storage() { }

    static class StringUtil {
        static boolean isEmpty(String str) {
            return str == null || str.length() == 0;
        }
    }

    private static SharedPreferences getSharedPreference(Context ctx) {
        return ctx.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    public static boolean addBoolean(Context ctx, String key, boolean value) {
        SharedPreferences prefs = getSharedPreference(ctx);
        return prefs != null && prefs.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key) {
        return !(ctx == null || StringUtil.isEmpty(key)) && getSharedPreference(ctx).getBoolean(key, false);
    }
}
