package com.coign.security;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class RecievedSmsDetailsActivity extends ListActivity {
	SQLiteDatabase db;
	MyDBHelper helper;
	SimpleCursorAdapter cadapter;
	ListView listView;
	String number,name;
	Cursor cursor;
	final Context context = this;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.recievedsmsdetails);
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.recievedsmstitlebar);
		 
		listView = (ListView) findViewById(android.R.id.list);
		helper = new MyDBHelper(this);
		db = helper.getWritableDatabase();

		Cursor cursor = db
				.query("smsdetails", new String[] { "_id", "snumber", "sname",
						"smsmessage", "dat" }, null, null, "sname", null, null);
		startManagingCursor(cursor);
		if(cursor.getCount()==0)
		{
			Toast.makeText(getApplicationContext(), "No messages",
					Toast.LENGTH_LONG).show();

	
			

		}
		else
		{
		cadapter = new SimpleCursorAdapter(this, R.layout.mylist, cursor,
				new String[] { "sname", "snumber" }, new int[] { R.id.text1,
						R.id.text2 });

		this.setListAdapter(cadapter);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) parent.getItemAtPosition(position);
				
			 number=cursor.getString(cursor.getColumnIndex("snumber"));
			name=cursor.getString(cursor.getColumnIndex("sname"));
				
			
				

			Intent i = new Intent(RecievedSmsDetailsActivity.this,
						LatLong1Activity.class);
				
				i.putExtra("sms_number", number);
				i.putExtra("sms_name",name);
				startActivity(i);

			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
				Cursor cursor = (Cursor) parent.getItemAtPosition(position);
				final int item_id = cursor.getInt(cursor
						.getColumnIndex("_id"));
				 final String num=cursor.getString(cursor.getColumnIndex("snumber"));
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
						
							
								
								
								
								Toast.makeText(getApplicationContext(),
										"contact deleted",
										Toast.LENGTH_LONG).show();
								refresh();
								
								
							
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
	
	public void refresh()
	
    {
		/*cursor.requery();
		 cadapter.notifyDataSetChanged();*/

		Cursor cursor = db
				.query("smsdetails", new String[] { "_id", "snumber", "sname",
						"smsmessage", "dat" }, null, null, "sname", null, null);
		startManagingCursor(cursor);
		cadapter = new SimpleCursorAdapter(this, R.layout.mylist, cursor,
				new String[] { "sname", "snumber" }, new int[] { R.id.text1,
						R.id.text2 });

		this.setListAdapter(cadapter);
		cadapter.notifyDataSetChanged();
		}
		
		
	
	
	public void backbutton(View v) {
		finish();
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			exitByBackKey();

			// moveTaskToBack(false);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void exitByBackKey() {

		AlertDialog alertbox = new AlertDialog.Builder(this)
				.setMessage("Do you want to exit application?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							// do something when the button is clicked
							public void onClick(DialogInterface arg0, int arg1) {

								  Intent intent = new Intent(Intent.ACTION_MAIN);
			                        intent.addCategory(Intent.CATEGORY_HOME);
			                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                        startActivity(intent);
			                        finish();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).show();

	}


	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		db.close();
	}


}
