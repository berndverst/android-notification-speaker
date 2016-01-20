package ly.readon.notifier;

import android.content.Context;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import ly.readon.notifier.parser.FacebookMessengerParser;
import ly.readon.notifier.parser.GoogleHangoutsParser;
import ly.readon.notifier.parser.NotificationParser;
import ly.readon.notifier.util.Storage;

import java.util.HashMap;
import java.util.Locale;

public class NotificationSpeechService extends NotificationListenerService {
    private final String TAG = NotificationSpeechService.class.getClass().getSimpleName();

    private static TextToSpeech speaker;

    private static String lastNotificationKey = "";
    private static long lastNotificationTimeMillis = System.currentTimeMillis();

    private HashMap<String, NotificationParser> notificationParsersMap;

    private HashMap<String, NotificationParser> createNotificationParsersMap()
    {
        HashMap<String, NotificationParser> map = new HashMap<>();

        // instantiate every NotificationParser subclass and add to map
        // the key needs to be NotificationParser.getPackageName()

        NotificationParser fbMessengerParser = new FacebookMessengerParser();
        map.put(fbMessengerParser.getPackageName(), fbMessengerParser);

        NotificationParser googleHangoutsParser = new GoogleHangoutsParser();
        map.put(googleHangoutsParser.getPackageName(), googleHangoutsParser);

        return map;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init all Notification Profiles: Facebook Messenger, etc
        notificationParsersMap = createNotificationParsersMap();

        // init Text To Speech Service
        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int ttsLanguageStatus = speaker.setLanguage(Locale.US);

                    if (ttsLanguageStatus == TextToSpeech.LANG_NOT_SUPPORTED || ttsLanguageStatus == TextToSpeech.LANG_MISSING_DATA) {
                        Toast.makeText(getApplicationContext(), "Functionality not available", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Functionality not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        TODO: Add support for output to SONOS:
        - need to call synthesizeToFile to create audio file
        - need to use unofficial SONOS API to send audio file to speaker(s)
         */
    }

    public static boolean hasNotificationListenerServicePermission(Context a) {
        return Settings.Secure.getString(a.getContentResolver(), "enabled_notification_listeners")
                .contains(a.getApplicationContext().getPackageName());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if ((!Storage.getBoolean(this, NotificationSpeechApplication.ALLOW_NOTIFICATION_SPEECH)) ||
                (!hasNotificationListenerServicePermission(this)))
            return;

        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        if (!notificationParsersMap.containsKey(sbn.getPackageName()))
            // TODO: Add a default parser
            return;

        NotificationParser parser = notificationParsersMap.get(sbn.getPackageName());

        if (!parser.shouldSpeak(sbn))
                return;

        if (parser.needsAnnouncement(sbn)) {
            String announcement = parser.formatNotificationAnnouncementForSpeech(sbn);
            speaker.speak(parser.getHumanName() + " Notification", TextToSpeech.QUEUE_ADD, null, null);
            speaker.playSilentUtterance(150, TextToSpeech.QUEUE_ADD, null);
            speaker.speak(announcement, TextToSpeech.QUEUE_ADD, null, null);
            speaker.playSilentUtterance(300, TextToSpeech.QUEUE_ADD, null);
        }
        String message = parser.formatNotificationMessageForSpeech(sbn);
        speaker.speak(message, TextToSpeech.QUEUE_ADD, null, null);
    }


    public static void setLastNotificationKey(String key) {
        NotificationSpeechService.lastNotificationKey = key;
    }

    public static String getLastNotificationKey() {
        return NotificationSpeechService.lastNotificationKey;
    }

    public static void setLastNotificationTimeMillis(long timeMillis) {
        NotificationSpeechService.lastNotificationTimeMillis = timeMillis;
    }

    public static long getLastNotificationTimeMillis() {
        return NotificationSpeechService.lastNotificationTimeMillis;
    }
}
