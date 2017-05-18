package me.henry.uicollections.quickindex;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasicAdapter<T> extends BaseAdapter {
protected ArrayList<T> list;
protected Context context;
public BasicAdapter(ArrayList<T>list,Context context){
	super();
	this.list=list;
	this.context=context;
	
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return convertView;
	}

}
