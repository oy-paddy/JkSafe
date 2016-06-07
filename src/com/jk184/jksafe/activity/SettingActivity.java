package com.jk184.jksafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jk184.jksafe.R;
import com.jk184.jksafe.view.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		findView();
		init();
	}

	private void findView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);

	}

	private void init() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = sp.getBoolean("auto_update", true);
		if (autoUpdate) {
			sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("自动更新已关闭");
					sp.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("自动更新已开启");
					sp.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}

}
