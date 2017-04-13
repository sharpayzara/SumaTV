package com.ddm.live.activities;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.ui.widget.CustomDialog;
import com.ddm.live.ui.widget.swipemenulistview.SwipeMenu;
import com.ddm.live.ui.widget.swipemenulistview.SwipeMenuCreator;
import com.ddm.live.ui.widget.swipemenulistview.SwipeMenuItem;
import com.ddm.live.ui.widget.swipemenulistview.SwipeMenuListView;

import java.util.List;

public class GagListActivity extends Activity {

	private List<ApplicationInfo> mAppList;
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private CustomDialog deteleGagDialog;
	private CustomDialog.Builder deteleGagBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gap_list_layout);

		mAppList = getPackageManager().getInstalledApplications(0);

		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(GagListActivity.this,"详细信息", Toast.LENGTH_SHORT).show();
			}
		});
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
//				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//						0xCE)));
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set item title
				deleteItem.setTitle("取消关注");
				// set item title fontsize
				deleteItem.setTitleSize(14);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(deleteItem);

			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				ApplicationInfo item = mAppList.get(position);
				switch (index) {
				case 0:
					// open
//					open(item);
//					showDialog();
					mAppList.remove(position);
					mAdapter.notifyDataSetChanged();
					break;
				case 1:
					// delete
//					delete(item);
//					mAppList.remove(position);
//					mAdapter.notifyDataSetChanged();
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
//		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//										   int position, long id) {
//				Toast.makeText(getApplicationContext(), position + " long click", 0).show();
//				return false;
//			}
//		});

	}
//	public void showDialog() {
//		deteleGagBuilder = new CustomDialog.Builder(this);
//		deteleGagBuilder.setMessage(null);
//		deteleGagBuilder.setTitle("取消禁言？");
//		deteleGagBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				//禁言实现代码
//			}
//		});
//		deteleGagBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		deteleGagDialog = deteleGagBuilder.create(R.layout.gag_dialog_layout);
//		deteleGagDialog.show();
//	}
	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
