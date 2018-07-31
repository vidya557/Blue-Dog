package com.coign.security;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowMapActivity extends Activity {

	double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.smsmapdetails);
		TextView tvdate = (TextView) findViewById(R.id.date);

		TextView tvnumber = (TextView) findViewById(R.id.number);

		TextView tvlocation = (TextView) findViewById(R.id.location);

		String date = getIntent().getStringExtra("SMS_DATE");

		String location = getIntent().getStringExtra("SMS_BODY");

		String number = getIntent().getStringExtra("SMS_NUMBER");

		String lati = getIntent().getStringExtra("sms_latitude");

		String longi = getIntent().getStringExtra("sms_longitude");

		latitude = Double.parseDouble(lati);
		longitude = Double.parseDouble(longi);
		
		
         //getAddress(latitude,longitude);
		

		//tvnumber.setText("Called on:"+date+ "\n" + "Phone no:"+number+ "\n" + "Location:" +location);
		tvnumber.setText("Phone no: " + number);
		tvlocation.setText("Location: " +getCompleteAddressString(latitude,longitude));

	 tvdate.setText("Called on: "+date);

		

	}
	

	public void mapbuttonclick(View v) {

		String name = "my location";
		Intent mapintent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("geo:0,0?q=" + latitude + "," + longitude + " ("
						+ name + ")"));
		
		mapintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(mapintent);

	}
	
	 
	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
     }

	

