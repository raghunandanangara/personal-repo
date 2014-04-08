package com.example.app1;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class App1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_app1);


		Button launchContacts = (Button) findViewById(R.id.button1);

		launchContacts.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
				i.setType(ContactsContract.Contacts.CONTENT_TYPE);   
				startActivity(i);
			}
		});
	}
}
