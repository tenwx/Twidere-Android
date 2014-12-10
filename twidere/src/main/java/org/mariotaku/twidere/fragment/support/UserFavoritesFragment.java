/*
 * Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
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

package org.mariotaku.twidere.fragment.support;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.Loader;

import org.mariotaku.twidere.loader.support.UserFavoritesLoader;
import org.mariotaku.twidere.model.ParcelableStatus;

import java.util.List;

import static org.mariotaku.twidere.util.Utils.isSameAccount;

/**
 * Created by mariotaku on 14/12/2.
 */
public class UserFavoritesFragment extends ParcelableStatusesFragment {

    @Override
    protected void onReceivedBroadcast(Intent intent, String action) {
        switch (action) {
            case BROADCAST_STATUS_FAVORITE_DESTROYED: {
                final Context context = getActivity();
                final ParcelableStatus status = intent.getParcelableExtra(EXTRA_STATUS);
                final Bundle args = getArguments();
                if (context == null || status == null || args == null) return;
                final long userId = args.getLong(EXTRA_USER_ID, -1);
                final String screenName = args.getString(EXTRA_SCREEN_NAME);
                if (isSameAccount(context, status.account_id, userId)
                        || isSameAccount(context, status.account_id, screenName)) {
                    deleteStatus(status.id);
                }
                return;
            }
        }
        super.onReceivedBroadcast(intent, action);
    }

    @Override
    protected void onSetIntentFilter(IntentFilter filter) {
        super.onSetIntentFilter(filter);
        filter.addAction(BROADCAST_STATUS_FAVORITE_DESTROYED);
    }

    @Override
    public Loader<List<ParcelableStatus>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        final Context context = getActivity();
        final long accountId = args.getLong(EXTRA_ACCOUNT_ID, -1);
        final long maxId = args.getLong(EXTRA_MAX_ID, -1);
        final long sinceId = args.getLong(EXTRA_SINCE_ID, -1);
        final long userId = args.getLong(EXTRA_USER_ID, -1);
        final String screenName = args.getString(EXTRA_SCREEN_NAME);
        final int tabPosition = args.getInt(EXTRA_TAB_POSITION, -1);
        return new UserFavoritesLoader(context, accountId, userId, screenName, maxId, sinceId,
                getAdapterData(), null, tabPosition);
    }

}