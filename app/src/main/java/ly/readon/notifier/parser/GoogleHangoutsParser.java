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
