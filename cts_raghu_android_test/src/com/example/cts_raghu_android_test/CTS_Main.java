package com.example.cts_raghu_android_test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;

public class CTS_Main extends Activity {

	/** Elements in activity_main.xml*/
	private ListView lstView;
	private Button refreshButton;

	/** Array list to capture JSON data*/
	private ArrayList<DataModelRow> arrNews ;

	/** Layout inflater instance*/
	private LayoutInflater lf;
	private Context mContext = this;

	/** Adapter to populate array list in Listview*/
	private DataAdapter va;

	/** Volley Library Variables*/
	private RequestQueue mRequestQueue;
	ImageLoader.ImageCache imageCache;
	ImageLoader imageLoader;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cts_main);
		

		lf = LayoutInflater.from(this);

		/** Using Memory Cache(RAM) of Volley Library only
		 * If images are in high number you need to implement Disk Cache also
		 **/
		imageCache = new BitmapLruCache();
		mRequestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mRequestQueue, imageCache);

		/** Initializing Arraylist and Adapter*/
		arrNews = new ArrayList<DataModelRow>();
		va = new DataAdapter(lf, arrNews, imageLoader);
		
		lstView = (ListView) findViewById(R.id.listView);


		/** Once refresh button is pressed clear all the data in arraylist
		 * and start JSON query again
		 **/
		refreshButton = (Button) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				arrNews.clear();
				va.clearAdapter();
				//va.notifyDataSetChanged();
				
				//lstView.setAdapter(null);
				new AsycTaskJsonParser(va, arrNews, getResources(), mContext);
				lstView.setAdapter(va);
				//lstView.refreshDrawableState();
			}
		});



		AsycTaskJsonParser task_readJSONfromFileParse = new AsycTaskJsonParser(va, arrNews, getResources(), mContext);
		String returned_string = null;
		try {
			returned_string = task_readJSONfromFileParse.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		lstView.setAdapter(va);
		
		setTitle(returned_string);
	}

}
