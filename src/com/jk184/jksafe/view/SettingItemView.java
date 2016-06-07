package com.jk184.jksafe.view;

import com.jk184.jksafe.R;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.jk184.jksafe";

	String TAG = "SettingItemView";

	private TextView tv_title;
	private TextView tv_desc;
	private CheckBox cb_status;

	private String title;

	private String desc_on;

	private String desc_off;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		int attributeCount = attrs.getAttributeCount();

		setAttribute(attrs);
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		cb_status = (CheckBox) findViewById(R.id.cb_status);

		setTitle(title);

	}

	private void setAttribute(AttributeSet attrs) {
		title = attrs.getAttributeValue(NAMESPACE, "title");
		desc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		desc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	public void setDesc(String desc) {
		tv_desc.setText(desc);
	}

	public void setChecked(boolean check) {
		cb_status.setChecked(check);
		if (check) {
			setDesc(desc_on);
		} else {
			setDesc(desc_off);
		}
	}

	public boolean isChecked() {
		return cb_status.isChecked();
	}

}
