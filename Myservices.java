package com.coign.security;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class Myservices extends Service {
	String num;
	String phoneNumber;
	 boolean ringing = false;
	 
	    boolean offhook = false;
	    public static String missedcalnumber;
	    
	    SQLiteDatabase db;
		MyDBHelper helper;
		
	
	    public  String storednumber;
	    
	 	Cursor c,mCallCursor;
	 	 
	 	  public   String text;
	 	 GPSTracker gps;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		 registerReceiver(new BroadcastReceiver() {
				

			    @Override
			    public void onReceive(Context context, Intent intent)
			    {
			    	 try{
			           TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			           telephony.listen(new PhoneStateListener(){
			               @Override
			               public void onCallStateChanged(int state, String incomingNumber) {
			                   
			               	 super.onCallStateChanged(state, incomingNumber);
			       
			               	 switch(state){
			                    case TelephonyManager.CALL_STATE_RINGING:
			                    	
			                    	ringing = true;
			                  	  
			                        offhook = false;

			          
			           
			                    	/* mLastState = TelephonyManager.CALL_STATE_RINGING;*/

			                        

			                         break;
			                 

			                    case TelephonyManager.CALL_STATE_OFFHOOK:
			                    
			                    	offhook = true;

			                        ringing = false;

			                    	/*mLastState = TelephonyManager.CALL_STATE_OFFHOOK;*/
			                         

			                          break;

			                  
			                    case TelephonyManager.CALL_STATE_IDLE:
			                    
			                     /* if (mLastState != TelephonyManager.CALL_STATE_IDLE)*/	
			                    	if(ringing&&(!offhook))
		                          {  
			                    		
			                    		  ringing = false;

				                          offhook = false; 
			                    	  missedcalnumber=incomingNumber;
			                    	  missedcalnumber=missedcalnumber.replaceAll(" ","");
			                    	 

			                		  Toast.makeText(getApplicationContext(), "you have a missed call:"+ missedcalnumber , Toast.LENGTH_LONG).show();
			                		 
			                          
			                          //sendingdata(missedcalnumber);

		                              try {
		                            	  
		                            	  helper = new MyDBHelper(getBaseContext());
		                      			db = helper.getWritableDatabase();
		                                  Cursor c = db.rawQuery("select phnumber from addcontact", null);
		                                  if(c!=null)
		              				    {
		              				        c.moveToFirst();
		              				        {
		              				            do {
		              				            	 
		              				               
		              				             String  number=c.getString(c.getColumnIndex("phnumber"));
		              				            
		              				                if( (missedcalnumber).equals(number))
		              				          
		              				                {  
		              				                	 gps = new GPSTracker(getBaseContext());
		              				                
		              				                    double lat=gps.latitude;
		              				                    double lon = gps.longitude;
		              				                     text = "Latitude:"+lat+","+"Longitude:"+lon;
		              				                     System.out
																.println("lalithaaaaaaaaaaaaaaaa"+text);
		              				                     
		              				                   SmsManager smsManager = SmsManager.getDefault();
		     		                                  smsManager.sendTextMessage(missedcalnumber, null, text, null, null);
		     		                                  Toast.makeText(getApplicationContext(), "SMS Sent!",
		     		                                          Toast.LENGTH_LONG).show();
		              				                     
		              				                 
		              				                }
		              				                	 
		                                
		                                            } 
		              				           while (c.moveToNext());
		              				        }
		              				    }
		                              }catch (Exception e) {
		                                  Toast.makeText(getApplicationContext(),
		                                          "SMS failed, please try again later!",
		                                          Toast.LENGTH_LONG).show();
		                                  e.printStackTrace();
		                              }

		                     

			                             
		                          }  


			                   /* mLastState = TelephonyManager.CALL_STATE_IDLE;*/

			                         break;
			                         
			                    default:
			                   	 
			                        
			                   	 
			                        break;
			                      
			                   	       
			                   	   
			                   	   
			                      
			               }
			               }
			             
			           },PhoneStateListener.LISTEN_CALL_STATE);
			           db.close();
			           
			       	 }
			       	 catch (Exception e) {
			                
			                e.printStackTrace();
			            }
			           
			           
			       }
			       
			      
			      

			  },new IntentFilter("android.intent.action.PHONE_STATE"));
			
	
		
		
		super.onCreate();
	}


   

	@Override
	public void onStart(Intent intent, int startId) {
		
	
		
		        
		    
		 }	
		/*  try {
			SmsManager smsManager = SmsManager.getDefault();
			  smsManager.sendTextMessage(phoneNumber, null, "text", null, null);
			  Toast.makeText(this, "SMS Sent!",
			          Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			 Toast.makeText(getApplicationContext(),
                     "SMS failed, please try again later!",
                     Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}*/
		// TODO Auto-generated method stub  
		
	

	
 /* public void sendingdata(String missedcalnumber)
  {
	  
	  String SENT = "SMS_SENT";
      String DELIVERED = "SMS_DELIVERED";

      PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
          new Intent(SENT), 0);

      PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
          new Intent(DELIVERED), 0);
	   
      
      registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				switch (getResultCode())
              {
                  case Activity.RESULT_OK:
                      Toast.makeText(getBaseContext(), "SMS sent", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                      Toast.makeText(getBaseContext(), "Generic failure", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NO_SERVICE:
                      Toast.makeText(getBaseContext(), "No service", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NULL_PDU:
                      Toast.makeText(getBaseContext(), "Null PDU", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_RADIO_OFF:
                      Toast.makeText(getBaseContext(), "Radio off", 
                              Toast.LENGTH_SHORT).show();
                      break;
              }
				
				
				
			}
		}, new IntentFilter(SENT));
                  
      
      registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				 switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case Activity.RESULT_CANCELED:
	                        Toast.makeText(getBaseContext(), "SMS not delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break;                        
	                }
			}
		}, new IntentFilter(DELIVERED));
        
      
      
      SmsManager sms = SmsManager.getDefault();
    
    
      sms.sendTextMessage(phoneNumber, null,"hello", sentPI, deliveredPI); 
	  
  }
*/
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "deleted", 20).show();
		super.onDestroy();
	}

}
