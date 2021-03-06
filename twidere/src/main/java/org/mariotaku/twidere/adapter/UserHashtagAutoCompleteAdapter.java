/*
 * 				Twidere - Twitter client for Android
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

package org.mariotaku.twidere.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.provider.TwidereDataStore.Suggestions;
import org.mariotaku.twidere.util.MediaLoaderWrapper;
import org.mariotaku.twidere.util.SharedPreferencesWrapper;
import org.mariotaku.twidere.util.UserColorNameManager;
import org.mariotaku.twidere.util.dagger.ApplicationModule;
import org.mariotaku.twidere.util.dagger.DaggerGeneralComponent;
import org.mariotaku.twidere.view.ProfileImageView;

import javax.inject.Inject;


public class UserHashtagAutoCompleteAdapter extends SimpleCursorAdapter implements Constants {

    private static final String[] FROM = new String[0];
    private static final int[] TO = new int[0];

    @Inject
    MediaLoaderWrapper mProfileImageLoader;
    @Inject
    SharedPreferencesWrapper mPreferences;
    @Inject
    UserColorNameManager mUserColorNameManager;

    private final boolean mDisplayProfileImage;

    private int mTypeIdx, mIconIdx, mTitleIdx, mSummaryIdx, mExtraIdIdx;
    private long mAccountId;
    private char mToken;

    public UserHashtagAutoCompleteAdapter(final Context context) {
        super(context, R.layout.list_item_auto_complete, null, FROM, TO, 0);
        DaggerGeneralComponent.builder().applicationModule(ApplicationModule.get(context)).build().inject(this);
        mDisplayProfileImage = mPreferences.getBoolean(KEY_DISPLAY_PROFILE_IMAGE, true);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        if (isCursorClosed()) return;
        final TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        final TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        final ProfileImageView icon = (ProfileImageView) view.findViewById(android.R.id.icon);

        // Clear images in order to prevent images in recycled view shown.
        icon.setImageDrawable(null);

        if (Suggestions.AutoComplete.TYPE_USERS.equals(cursor.getString(mTypeIdx))) {
            text1.setText(mUserColorNameManager.getUserNickname(cursor.getLong(mExtraIdIdx), cursor.getString(mTitleIdx)));
            text2.setText("@" + cursor.getString(mSummaryIdx));
            if (mDisplayProfileImage) {
                final String profileImageUrl = cursor.getString(mIconIdx);
                mProfileImageLoader.displayProfileImage(icon, profileImageUrl);
            } else {
                mProfileImageLoader.cancelDisplayTask(icon);
            }

            icon.clearColorFilter();
        } else {
            text1.setText("#" + cursor.getString(mTitleIdx));
            text2.setText(R.string.hashtag);

            icon.setImageResource(R.drawable.ic_action_hashtag);
            icon.setColorFilter(text1.getCurrentTextColor(), Mode.SRC_ATOP);
        }
        icon.setVisibility(mDisplayProfileImage ? View.VISIBLE : View.GONE);
        super.bindView(view, context, cursor);
    }

    public void closeCursor() {
        final Cursor cursor = getCursor();
        if (cursor == null) return;
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    public CharSequence convertToString(final Cursor cursor) {
        if (isCursorClosed()) return null;
        return cursor.getString(mSummaryIdx != -1 ? mSummaryIdx : mTitleIdx);
    }

    public boolean isCursorClosed() {
        final Cursor cursor = getCursor();
        return cursor == null || cursor.isClosed();
    }

    @Override
    public Cursor runQueryOnBackgroundThread(final CharSequence constraint) {
        if (TextUtils.isEmpty(constraint)) return null;
        char token = constraint.charAt(0);
        if (getNormalizedSymbol(token) == getNormalizedSymbol(mToken)) {
            final FilterQueryProvider filter = getFilterQueryProvider();
            if (filter != null) return filter.runQuery(constraint);
        }
        mToken = token;
        final Uri.Builder builder = Suggestions.AutoComplete.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(QUERY_PARAM_QUERY, String.valueOf(constraint.subSequence(1, constraint.length())));
        switch (getNormalizedSymbol(token)) {
            case '#': {
                builder.appendQueryParameter(QUERY_PARAM_TYPE, Suggestions.AutoComplete.TYPE_HASHTAGS);
                break;
            }
            case '@': {
                builder.appendQueryParameter(QUERY_PARAM_TYPE, Suggestions.AutoComplete.TYPE_USERS);
                break;
            }
            default: {
                return null;
            }
        }
        builder.appendQueryParameter(QUERY_PARAM_ACCOUNT_ID, String.valueOf(mAccountId));
        return mContext.getContentResolver().query(builder.build(), Suggestions.AutoComplete.COLUMNS,
                null, null, null);
    }


    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }

    @Override
    public Cursor swapCursor(final Cursor cursor) {
        if (cursor != null) {
            mTypeIdx = cursor.getColumnIndex(Suggestions.AutoComplete.TYPE);
            mTitleIdx = cursor.getColumnIndex(Suggestions.AutoComplete.TITLE);
            mSummaryIdx = cursor.getColumnIndex(Suggestions.AutoComplete.SUMMARY);
            mExtraIdIdx = cursor.getColumnIndex(Suggestions.AutoComplete.EXTRA_ID);
            mIconIdx = cursor.getColumnIndex(Suggestions.AutoComplete.ICON);
        }
        return super.swapCursor(cursor);
    }


    private static char getNormalizedSymbol(final char character) {
        switch (character) {
            case '\uff20':
            case '@':
                return '@';
            case '\uff03':
            case '#':
                return '#';
        }
        return '\0';
    }

}
