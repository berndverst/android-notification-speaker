package ly.readon.notifier;

import android.app.Application;

/**
 * @author : Bernd Verst(@berndverst)
 */
public class NotificationSpeechApplication extends Application {
    public final static String ALLOW_NOTIFICATION_SPEECH = "allow_notification_speech";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
