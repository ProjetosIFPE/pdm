package br.edu.ifpe.tads.pdm.projeto.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import br.edu.ifpe.tads.pdm.projeto.R;

/**
 * Created by Edmilson on 22/10/2016.
 */

public class NotificationUtil {

    public static void notify(Context context, int id, Intent intent, String contentTitle, String contentText, Boolean autoCancel) {
        NotificationManager  notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = createNotification(context, intent, contentTitle, contentText, autoCancel);

        notificationManager.notify(id, n);
    }

    public static Notification createNotification(Context context, Intent intent, String contentTitle, String contentText, Boolean autoCancel) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(autoCancel);

        return builder.build();
    }

}
