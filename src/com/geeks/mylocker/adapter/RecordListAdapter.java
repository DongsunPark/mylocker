package com.geeks.mylocker.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geeks.mylocker.R;
import com.geeks.mylocker.dao.Record;

public class RecordListAdapter extends ArrayAdapter<Record> {
	
	protected final String TAG = getClass().getSimpleName();
	
	private final Context context;
	private final List<Record> records;

	public RecordListAdapter(Context context, List<Record> records) {
		super(context, R.layout.rowlayout_record, records);
		this.context = context;
		this.records = records;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.rowlayout_record, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.rowlayout_record_name);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.rowlayout_record_icon);
		
		Record record = records.get(position);
		Log.d(TAG, record.getName());
		textView.setText(record.getName());
		
		// Change the icon for Windows and iPhone
		//imageView.setImageResource(R.drawable.no);
		
		return rowView;
	}

}
