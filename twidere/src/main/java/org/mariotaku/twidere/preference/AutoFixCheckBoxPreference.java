package org.mariotaku.twidere.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AutoFixCheckBoxPreference extends CheckBoxPreference {

	public AutoFixCheckBoxPreference(final Context context) {
		super(context);
	}

	public AutoFixCheckBoxPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoFixCheckBoxPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
		try {
			super.onSetInitialValue(restoreValue, defaultValue);
		} catch (final ClassCastException e) {
			final SharedPreferences prefs = getSharedPreferences();
			if (prefs != null) {
				prefs.edit().remove(getKey()).apply();
			}
		}
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		TextView textView = (TextView) view.findViewById(android.R.id.title);
		if (textView != null) {
			textView.setSingleLine(false);
		}
	}
}
