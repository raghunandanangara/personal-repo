package com.example.app2;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class App2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_app2);

		Button launch_gallery = (Button) findViewById(R.id.button1);

		launch_gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
				intent.setType("image/*");
				startActivity(Intent.createChooser(intent, "Select Picture"));
			}
		});
	}
}
