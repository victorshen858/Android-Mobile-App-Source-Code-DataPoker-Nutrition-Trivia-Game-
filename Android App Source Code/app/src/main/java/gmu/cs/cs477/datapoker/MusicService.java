package gmu.cs.cs477.datapoker;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class MusicService extends Service {
    public static MediaPlayer playerM;
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        playerM = MediaPlayer.create(this,
                R.raw.magiclights);
        playerM.setLooping(false);

    }

    public void stopService() {
        playerM.stop();
    }


    @Override
       public void onDestroy() {
        playerM.stop();   super.onDestroy();
    }

    @Override
    public int onStartCommand (Intent intent, int flags,
                               int startid) {
        playerM.start();
        playerM.start();
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
