package com.example.cts_raghu_android_test;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/** Adapter for managing DataModelRow*/
public class DataAdapter extends BaseAdapter{

	private ArrayList<DataModelRow> arrNews ;
	private LayoutInflater lf;
	private ImageLoader imageLoader;

	public DataAdapter(LayoutInflater lf2, ArrayList<DataModelRow> arrNews2,
			ImageLoader imageLoader2) {
		this.lf = lf2;
		this.arrNews = arrNews2;
		this.imageLoader = imageLoader2;
	}
	
	public void clearAdapter() {
		arrNews.clear();
	}

	@Override
	public int getCount() {
		return arrNews.size();
	}

	@Override
	public Object getItem(int i) {
		return arrNews.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh ;
		if(view == null){
			vh = new ViewHolder();
			view = lf.inflate(R.layout.row_listview,null);
			vh.tvImage = (NetworkImageView) view.findViewById(R.id.imgImage);
			vh.tvTitle = (TextView) view.findViewById(R.id.txtTitle);
			vh.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
			view.setTag(vh);
		}
		else{
			vh = (ViewHolder) view.getTag();
		}

		//DataModelRow nm = arrNews.get(i);
		DataModelRow nm = (DataModelRow) getItem(i);

		//If Image URI is null then hide corresponding Imageview
		String url = nm.getImageHref();
		if(url!=null)
		{
			vh.tvImage.setVisibility(View.VISIBLE);
			vh.tvImage.setImageUrl(nm.getImageHref(), imageLoader);
			vh.tvImage.setDefaultImageResId(R.drawable.no_image);
			vh.tvImage.setErrorImageResId(R.drawable.error_image1);
		}
		else
		{
			vh.tvImage.setVisibility(View.GONE);
		}
		vh.tvTitle.setText(nm.getTitle());
		vh.tvDesc.setText(nm.getDescription());
		return view;
	}

	//To Hold views in a Listview
	class ViewHolder{
		//This ImageView is Volley Library special ImageView
		NetworkImageView tvImage;
		TextView tvTitle;
		TextView tvDesc;
	}
}
