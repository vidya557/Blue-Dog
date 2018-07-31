package com.coign.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LatLong1Activity extends ListActivity {

	ArrayList<String> arraylist = new ArrayList<String>();

	SQLiteDatabase db;
	MyDBHelper helper;
	ListView listView;
	 ArrayAdapter<String> cadapter;
	String lvmessageitem;
	String[] aray, aray1, aray2, aray3, aray4, ar;
	String lati, longi;
	Double latitude, longitude;
	final Context context = this;
   Cursor c;
	String number,name;
	 String adres;
	

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
		

		try {
			 c = db.rawQuery(
					"select _id,snumber,sname,smsmessage,dat from smsdetails where snumber='"
							+ number + "' order by dat desc", null);

		
				

					
					 if(c!=null)
					    {
					        if(c.moveToFirst())
					        {
					            do {
					            	 
					            	String s = c.getString(c
											.getColumnIndex("smsmessage"));
					            	String date = c.getString(c
											.getColumnIndex("dat"));
					            	
					            	aray = s.split("[,]");
									System.out.println(aray);
									aray1 = aray[0].split("[:]");
									aray2 = aray[1].split("[:]");

									lati = aray1[aray1.length - 1];
									longi = aray2[aray2.length - 1];
									

									  latitude=Double.parseDouble(lati);
				                      longitude=Double.parseDouble(longi);
				                      
				                      adres=getCompleteAddressString(latitude,longitude);
					               
					               
					                arraylist.add(adres+date);
					            } while (c.moveToNext());
					        }
					      
					        cadapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arraylist);
						    listView.setAdapter(cadapter);
					    }
					
					 if(adres.contentEquals(""))
						{
						 Toast.makeText(getApplicationContext(), "No Network Connection", 90).show();
					
	             	}
					 else
					 {
						 listView.setOnItemClickListener(new OnItemClickListener() {
							 
								
								public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
									
									
									Intent i = new Intent(LatLong1Activity.this,
											SmsMapdisplayActivity.class);

									
									i.putExtra("listview_latitude", lati);
									i.putExtra("listview_longitude", longi);
									startActivity(i);
								

							
						
								}
							

							});
						  
					 }
		 
				/*	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

							public boolean onItemLongClick(AdapterView<?> parent,
									View view, int position, long id) {
								
								  Object o = listView.getItemAtPosition(position);
								String adr=o.toString();
								   aray3=adr.split("[\n]");
							
							db.delete("smsdetails", "dat=" + smsdate,
									null);
								arraylist.remove(position);
								
								refresh();
								
								Toast.makeText(getApplicationContext(), "o", Toast.LENGTH_LONG).show();
							
								return true;
							}
							});*/
						
							   
			
		}

						
				catch (Exception e) {
						            e.printStackTrace();
						            
						        }
	
}

	
	
	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder( context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
						
						
		
					
	public void backbutton(View v) {
		finish();
		
	}

	
	public void refresh()
    {
		c.requery();
		 cadapter.notifyDataSetChanged();
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
		
		
