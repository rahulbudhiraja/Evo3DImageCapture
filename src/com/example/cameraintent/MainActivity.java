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
import android.graphics.Bitmap.CompressFormat;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            	
            	PackageManager pm = getBaseContext().getPackageManager();
            	
            if(event == FileObserver.CLOSE_WRITE && !file.equals(".probe")){ // check if its a "create" and not equal to .probe because thats created every time camera is launched
            	{
            		Log.d("fds", "File created [" + android.os.Environment.getExternalStorageDirectory().toString() + "/DCIM/100MEDIA/" + file + "]");
            	i++;
            	Log.d("i value","i ="+i);
            	try
                {
                       
                  //int t=splitmpointojpg(Environment.getExternalStorageDirectory().toString() + "/DCIM/100MEDIA/" + file);
                  
                  String jpsfilename=Environment.getExternalStorageDirectory().toString() + "/DCIM/100MEDIA/" + file;
                  String jpgfilename=jpsfilename.substring(0,jpsfilename.length()-1)+"g";
                  
                  Log.d("File name","name "+jpsfilename);
                  
                  File from      = new File("",jpsfilename);
                  File to        = new File("",jpgfilename);
                  from.renameTo(to);
                             
                  Log.i("From path is", from.toString());
                  Log.i("To path is", to.toString());
                  
                  Bitmap fullsize_bitmap=BitmapFactory.decodeFile(jpgfilename);
                  Bitmap left_img=Bitmap.createBitmap(fullsize_bitmap, 0,0,fullsize_bitmap.getWidth()/2,fullsize_bitmap.getHeight());
                
                  Bitmap right_img=Bitmap.createBitmap(fullsize_bitmap,fullsize_bitmap.getWidth()/2,0,fullsize_bitmap.getWidth()/2,fullsize_bitmap.getHeight());
                  
                  OutputStream outputStream,outputStream2;
                  
          		try {
          			outputStream = new FileOutputStream ( Environment.getExternalStorageDirectory().getPath()+"/SimpleImageCapture/img_left.jpg");
          	        left_img.compress(CompressFormat.JPEG, 100, outputStream);
          		
          		  outputStream2 = new FileOutputStream ( Environment.getExternalStorageDirectory().getPath()+"/SimpleImageCapture/img_right.jpg");
          		right_img.compress(CompressFormat.JPEG, 100, outputStream2);
        		
          		
            	} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} 
                  
                  Log.d("yp", "blah");
                  
                  
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
	    
    System.loadLibrary("mposplit");
    
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
	
	
	 public native int splitmpointojpg(String path);
	 
	 
	}

	