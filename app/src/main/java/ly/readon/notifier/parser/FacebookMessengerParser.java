package ly.readon.notifier.parser;

import android.service.notification.StatusBarNotification;

/**
 * @author : Bernd Verst(@berndverst)
 */
public class FacebookMessengerParser extends NotificationParser {

    private final String PACKAGE_NAME = "com.facebook.orca";
    private final String HUMAN_NAME = "Facebook Messenger";

    @Override
    public String getPackageName() {
        return this.PACKAGE_NAME;
    }

    @Override
    public String getHumanName() {
        return this.HUMAN_NAME;
    }

    @Override
    public boolean shouldSpeak(StatusBarNotification sbn) {
        // This seems to avoid some generic Facebook Messenger notifications
        return (sbn.getId() < 20000);
    }
}
