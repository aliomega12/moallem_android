package ir.moallem.app;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

public class Mqttbackground extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
