package ly.readon.notifier.parser;

/**
 * @author : Bernd Verst(@berndverst)
 */
public class GoogleHangoutsParser extends NotificationParser {

    private final String PACKAGE_NAME = "com.google.android.talk";
    private final String HUMAN_NAME = "Google Hangouts";

    @Override
    public String getPackageName() {
        return this.PACKAGE_NAME;
    }

    @Override
    public String getHumanName() {
        return this.HUMAN_NAME;
    }
}
