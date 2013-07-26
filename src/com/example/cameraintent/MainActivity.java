package com.example.cameraintent;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888; 
	private ImageView imageView;
	static int i=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    this.imageView = (ImageView)this.findViewById(R.id.imageView1);
	    Button photoButton = (Button) this.findViewById(R.id.button1);
	    
	    
	    
	    
	    FileObserver observer = new FileObserver(android.os.Environment.getExternalStorageDirectory().toString() + "/DCIM/100MEDIA/") { // set up a file observer to watch this directory on sd card
            @SuppressWarnings("deprecation")
			@Override
        public void onEvent(int event, String file) {
            	Log.d("fired","fired"+" Event:"+event);
            	
//            	Intent intent = new Intent(getBaseContext(), MainActivity.class);
//            	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
//            	                                                //  the activity from a service
//            	intent.setAction(Intent.ACTION_MAIN);
//            	intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            	startActivity(intent);
            	
            	PackageManager pm = getBaseContext().getPackageManager();
            	
            if(event == FileObserver.CREATE && !file.equals(".probe")){ // check if its a "create" and not equal to .probe because thats created every time camera is launched
            	{
            		Log.d("fds", "File created [" + android.os.Environment.getExternalStorageDirectory().toString() + "/DCIM/100MEDIA/" + file + "]");
            	i++;
            	Log.d("i value","i ="+i);
            	try
                {
                  Intent it = pm.getLaunchIntentForPackage("com.example.cameraintent");

                  if (null != it)
                	  getBaseContext().startActivity(it);
                }

                catch (ActivityNotFoundException e)
                {//oops, no such application
                }
          
            	
            	}// fileSaved = "New photo Saved: " + file;
            }
        }
    };
    observer.startWatching(); // start the observer
	    
	    photoButton.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	String packageName = "com.android.camera"; //Or whatever package should be launched

	        	if(packageName.equals("com.android.camera")){ //Camera
	        	    try
	        	    {
	        	       
	        	    	Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.camera");

	        	    	intent.putExtra("android.intent.extras.CAMERA_FACING", 2);
	        	    	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        	        Log.d("Test","num"+Camera.getNumberOfCameras());
	        	        startActivity(intent);

	        	    }
	        	    catch(ActivityNotFoundException e){
	        	        Intent intent = new Intent();
	        	        ComponentName comp = new ComponentName("com.android.camera", "com.android.camera.CameraEntry");
	        	        intent.setComponent(comp);
	        	        startActivity(intent);
	        	    }
	        	}
	        	else{ //Any other
	        	    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
	        	    startActivity(intent);
	        	}
	        }
	    });
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    Log.d("Request code:"+requestCode,"tea");
	    
	    switch(requestCode){
	    case CAMERA_REQUEST:
	    
Log.d("yea","Passing");
Log.d("Fuck","yeap");
System.exit(1);
	        }
	    }
	}

	