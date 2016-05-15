package com.jk184.jksafe.activity;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public void showLongToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
}
