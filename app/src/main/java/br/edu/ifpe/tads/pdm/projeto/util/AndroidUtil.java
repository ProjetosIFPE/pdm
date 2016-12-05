package br.edu.ifpe.tads.pdm.projeto.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by Edmilson on 04/12/2016.
 */

public class AndroidUtil {

    private static String TAG = AndroidUtil.class.getSimpleName();

    public static String getVersionName(Context context) {
        String versionName = "";
        if (context != null) {
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
        return versionName;
    }
}
