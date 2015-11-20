/*
 * Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2015 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.model;

import android.content.Intent;

import org.mariotaku.twidere.TwidereConstants;

public class ComposingStatus implements TwidereConstants {

    public final String text, name, screen_name, in_reply_to_screen_name, in_reply_to_name;
    public final long in_reply_to_id;

    public ComposingStatus(final Intent intent) {
        text = intent.getStringExtra(EXTRA_TEXT);
        name = intent.getStringExtra(EXTRA_NAME);
        screen_name = intent.getStringExtra(EXTRA_SCREEN_NAME);
        in_reply_to_screen_name = intent.getStringExtra(EXTRA_IN_REPLY_TO_SCREEN_NAME);
        in_reply_to_name = intent.getStringExtra(EXTRA_IN_REPLY_TO_NAME);
        in_reply_to_id = intent.getLongExtra(EXTRA_IN_REPLY_TO_ID, -1);
    }
}
