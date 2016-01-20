package ly.readon.notifier;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ly.readon.notifier.util.Storage;

/**
 * @author : Bernd Verst(@berndverst)
 */
public class MainActivity extends AppCompatActivity {

    private Switch readNotificationsSwitch;
    private AlertDialog notificationServiceAccessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readNotificationsSwitch = (Switch) findViewById(R.id.read_notification_switch);
        initNotificationSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();
        readNotificationsSwitch.setChecked(Storage.getBoolean(this, NotificationSpeechApplication.ALLOW_NOTIFICATION_SPEECH));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification_settings:
                showAllowNotificationAccessDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initNotificationSwitch() {
        readNotificationsSwitch.setChecked(Storage.getBoolean(this, NotificationSpeechApplication.ALLOW_NOTIFICATION_SPEECH));
        readNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Storage.addBoolean(MainActivity.this, NotificationSpeechApplication.ALLOW_NOTIFICATION_SPEECH, isChecked);
                if (isChecked) {
                    // ON
                    if (!NotificationSpeechService.hasNotificationListenerServicePermission(MainActivity.this))
                        showAllowNotificationAccessDialog();
                } else {
                    // OFF
                }
            }
        });
    }

    private void showAllowNotificationAccessDialog() {
        if (notificationServiceAccessDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.allow_notification_access_title)
                    .setMessage(R.string.allow_notification_access_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.label_go_to_setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent notificationSettingIntent;
                            if (Build.VERSION.SDK_INT < 18)
                                notificationSettingIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            else
                                notificationSettingIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);

                            try {
                                startActivity(notificationSettingIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.this, R.string.notifications_settings_not_found, Toast.LENGTH_LONG);
                            }

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            readNotificationsSwitch.setChecked(false);
                            dialog.dismiss();
                        }
                    });
            notificationServiceAccessDialog = builder.create();
        }

        if (!notificationServiceAccessDialog.isShowing())
            notificationServiceAccessDialog.show();
    }
}
