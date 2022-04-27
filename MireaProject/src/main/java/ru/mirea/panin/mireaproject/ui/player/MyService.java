package ru.mirea.panin.mireaproject.ui.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import ru.mirea.panin.mireaproject.MainActivity;
import ru.mirea.panin.mireaproject.R;

public class MyService extends Service {
    public static final String CHANNEL_ID = "MyServiceChannel";
    private static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Player Service")
                .setContentText("Now playing")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        mediaPlayer.start();
        PlayerFragment.indicator = false;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        PlayerFragment.indicator = true;
        mediaPlayer.stop();
    }

    public static int getDuration(){
        while (mediaPlayer == null){ }
        return mediaPlayer.getDuration();
    }

    public static int getPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public static void setPosition(int i){
        if(mediaPlayer!=null)
            mediaPlayer.seekTo(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}