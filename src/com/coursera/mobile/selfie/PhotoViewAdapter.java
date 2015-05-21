package com.coursera.mobile.selfie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geeks.mylocker.R;

public class PhotoViewAdapter extends BaseAdapter {

	private ArrayList<Photo> list = new ArrayList<Photo>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public PhotoViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		Photo curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.photo_view, parent, false);
			holder.photo = (ImageView) newView.findViewById(R.id.photo);
			holder.name = (TextView) newView.findViewById(R.id.name);
			holder.createdDate = (TextView) newView.findViewById(R.id.created_date);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.photo.setImageBitmap(curr.getBitmap());
		holder.name.setText(curr.getName());
		String createdDate = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(curr.getCreatedDate());
		holder.createdDate.setText(createdDate);

		return newView;
	}

	static class ViewHolder {

		ImageView photo;
		TextView name;
		TextView createdDate;

	}
	
	public void add(Photo listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<Photo> getList() {
		return list;
	}

	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
}
