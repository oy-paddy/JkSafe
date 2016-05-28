package com.jk184.jksafe.activity;

import com.jk184.jksafe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {

	private GridView gv_home;

	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		findView();
	}

	private void findView() {
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				switch (position) {
				// 设置中心
				case 8:
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			return mItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			Item item;
			if (convertView == null) {
				convertView = View.inflate(HomeActivity.this,
						R.layout.item_home, null);
				item = new Item();
				item.iv_item = (ImageView) convertView
						.findViewById(R.id.iv_item);
				item.tv_item = (TextView) convertView
						.findViewById(R.id.tv_item);
				convertView.setTag(item);
			} else {
				item = (Item) convertView.getTag();
			}

			item.tv_item.setText(mItems[arg0]);
			item.iv_item.setImageResource(mPics[arg0]);

			return convertView;
		}

		class Item {
			private ImageView iv_item;
			private TextView tv_item;
		}

	}

}
