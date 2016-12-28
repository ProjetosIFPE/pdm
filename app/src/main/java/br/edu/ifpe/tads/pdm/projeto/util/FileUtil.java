package br.edu.ifpe.tads.pdm.projeto.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by EdmilsonS on 26/12/2016.
 */

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static File salvarImagem(Context context, Bitmap bitmap, String fileName) {

        File file = new File(context.getFilesDir(), fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            closeResources(fileOutputStream);
        }
        return file;
    }

    public static File getArquivoImagem(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file;
    }

    public static Boolean removerImagem(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        Boolean removeu = Boolean.FALSE;
        if (file != null) {
            removeu = file.delete();
        }
        return removeu;
    }

    private static void closeResources(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }
}
