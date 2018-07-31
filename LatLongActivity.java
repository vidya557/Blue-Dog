package com.coign.security;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LatLongActivity extends ListActivity {

	ArrayList<String> arraylist = new ArrayList<String>();

	SQLiteDatabase db;
	MyDBHelper helper;
	ListView listView;
	SimpleCursorAdapter cadapter;
	String lvmessageitem;
	String[] aray, aray1, aray2, aray3, aray4, ar;
	String lati, longi;
	Double latitude, longitude;
	final Context context = this;
   Cursor c;
	 String number,name;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.latlong);
		
		  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		 
		  
		
		listView = (ListView) findViewById(android.R.id.list);
		helper = new MyDBHelper(this);
		db = helper.getWritableDatabase();

		Bundle b = getIntent().getExtras();
		number = b.getString("sms_number");
		name=b.getString("sms_name");
		
	final TextView tv=(TextView)findViewById(R.id.Title);
	if ( tv != null ) {
       
           tv.setText(name);
           
       }
		

		System.out.println("long lati Activity number" + number);
		Toast.makeText(getApplicationContext(), "hai......" + number,
				Toast.LENGTH_LONG).show();
		
		Toast.makeText(getApplicationContext(), "hai......" + name,
				Toast.LENGTH_LONG).show();

		try {
			 c = db.rawQuery(
					"select _id,snumber,sname,smsmessage,dat from smsdetails where snumber='"
							+ number + "' order by dat desc", null);

			startManagingCursor(c);
			cadapter = new SimpleCursorAdapter(this, R.layout.mylist, c,
					new String[] { "smsmessage", "dat" }, new int[] {
							R.id.text1, R.id.text2 });

			this.setListAdapter(cadapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Cursor cursor = (Cursor) parent.getItemAtPosition(position);
					String s = cursor.getString(cursor
							.getColumnIndex("smsmessage"));

					System.out.println("listview value" + s);

					Toast.makeText(getApplicationContext(), "hai......" + s,
							Toast.LENGTH_LONG).show();
					aray = s.split("[,]");
					System.out.println(aray);
					aray1 = aray[0].split("[:]");
					aray2 = aray[1].split("[:]");

					lati = aray1[aray1.length - 1];
					longi = aray2[aray2.length - 1];

					System.out.println("latlongactivity latitude.........."
							+ lati);

					System.out.println("latlongactivity longitude.........."
							+ longi);

					Intent i = new Intent(LatLongActivity.this,
							SmsMapdisplayActivity.class);

					i.putExtra("sms_message", s);
					i.putExtra("listview_latitude", lati);
					i.putExtra("listview_longitude", longi);
					startActivity(i);

				}

			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					Cursor cursor = (Cursor) parent.getItemAtPosition(position);
					final int item_id = cursor.getInt(cursor
							.getColumnIndex("_id"));
			
					AlertDialog.Builder myDialog = new AlertDialog.Builder(
							context);
					myDialog.setTitle("Delete?");

					myDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								// do something when the button is clicked
								public void onClick(DialogInterface arg0,
										int arg1) {
									db.delete("smsdetails", "_id=" +item_id,
											null);
									refresh();
								
									
									
									
									Toast.makeText(getApplicationContext(),
											"contact deleted",
											Toast.LENGTH_LONG).show();
									
									
								
								}

							});
					myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // do nothing
				        }
				     });
					myDialog.show();

					return true;

				}

			});
		}

		catch (Exception e) {

			e.printStackTrace();
		}

	}
	public void backbutton(View v) {
		super.onBackPressed();
		
	}

	
	public void refresh()
    {
		c.requery();
		 cadapter.notifyDataSetChanged();
   }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		db.close();
	}

	

}
