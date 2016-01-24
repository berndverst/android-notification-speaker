/*
 * Copyright 2016 Bernd Verst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ly.readon.notifier.parser;

import android.app.Notification;
import android.service.notification.StatusBarNotification;

import ly.readon.notifier.NotificationSpeechService;

/**
 * @author : Bernd Verst(@berndverst)
 */
public abstract class NotificationParser {

    public abstract String getPackageName();

    public abstract String getHumanName();

    public boolean shouldSpeak(StatusBarNotification sbn) {
        return true;
    }

    public long getRepeatAnnouncementTimeMillis() {
       return 60000;  // 1 minute default
    }

    public boolean needsAnnouncement(StatusBarNotification sbn) {
        boolean shouldAnnounce = true;

        String newKey = createAnnouncementKeyForNotification(sbn);

        long currentTime = System.currentTimeMillis();
        if (NotificationSpeechService.getLastNotificationKey().equals(newKey)) {
            if (
                    (currentTime - NotificationSpeechService.getLastNotificationTimeMillis())
                            < getRepeatAnnouncementTimeMillis()
                    )
                shouldAnnounce = false;
        } else {
            NotificationSpeechService.setLastNotificationKey(newKey);
        }
        NotificationSpeechService.setLastNotificationTimeMillis(currentTime);

        return shouldAnnounce;
    }

    protected String createAnnouncementKeyForNotification(StatusBarNotification sbn) {
        return this.getPackageName() + ": " +
                sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE) + "";
    }

    public String formatNotificationMessageForSpeech(StatusBarNotification sbn){

        CharSequence message = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
        if (message == null)
            message = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);
        return (message == null) ? "" : message.toString();
    }

    public String formatNotificationAnnouncementForSpeech(StatusBarNotification sbn){
        String user = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();
        return "New message from " + user;
    }
}
