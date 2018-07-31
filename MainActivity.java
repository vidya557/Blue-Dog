package com.coign.security;




import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	SQLiteDatabase db;
	MyDBHelper helper;
	SimpleCursorAdapter cadapter;
	ListView listView;

	String phoneNo, s, phno;
	
	public static String missedcalnumber;


	  final Context context = this;
	Cursor cursor;
	int mLastState;
	
	 GPSTracker gps;

	 public   String text;
	 
	 boolean ringing = false;
	 
	    boolean offhook = false;
	    


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.activity_main);
          
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
            
            startService(new Intent(this, Myservices.class));
    		Intent mServiceIntent = new Intent();
    		mServiceIntent.setAction("com.coign.security.myservices");
    		  this.startService(mServiceIntent);
            
            helper = new MyDBHelper(this);
			db = helper.getWritableDatabase();
            
     
			
            
                     			listView = (ListView) findViewById(android.R.id.list);
		
			
			
			

			 cursor = db.query("addcontact", new String[] { "_id",
					"phnumber", "name" }, null, null, null, null, null);
			startManagingCursor(cursor);
			if(cursor.getCount()==0)
			{
				Toast.makeText(getApplicationContext(), "Please Click On Add Guardians Button For Registering Cotacts",
						Toast.LENGTH_LONG).show();

		
				

			}
			else
			{
				
				cadapter = new SimpleCursorAdapter(this, R.layout.mylist, cursor,
					new String[] { "name", "phnumber" }, new int[] {
							R.id.text1, R.id.text2 });

				this.setListAdapter(cadapter);
			
			}
			
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
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
									db.delete("addcontact", "_id=" + item_id,
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

				}

			});
			
			      

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void refresh()
    {
		cursor.requery();
		 cadapter.notifyDataSetChanged();
   }
	
	

	public void backbutton(View v) {
		finish();
		

	}


	


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void submit(View v) {
		try {
			Intent intent = new Intent(MainActivity.this,RegisteringNumber.class
					);
			startActivityForResult(intent, 10);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recievedsmsclick(View v) {
		try {
			Intent i = new Intent(this, RecievedSmsDetailsActivity.class);

			startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (10):
			
				
							cadapter = new SimpleCursorAdapter(this, R.layout.mylist, cursor,
									new String[] { "name", "phnumber" }, new int[] {
											R.id.text1, R.id.text2 });

								this.setListAdapter(cadapter);

			break;
		}
	}

	/*private String pad(String phoneNo) {
		// TODO Auto-generated method stub

		if (phoneNo.startsWith("+91")) {

			s = phoneNo;
			return s;

		} else {

			String smain = "+91" + phoneNo;

			return smain;
		}

	}
	*/
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
	


	@Override
	protected void onResume() {
		 
       
		 
  		super.onResume();
	}
	
	


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		
		super.onStart();
	}


	@Override
	protected void onPause() {
	
		
		
		
		super.onPause();
	}


	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		db.close();
	}
	
	
	
	
	
}
