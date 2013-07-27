#include <jni.h>
#include "opencv2/core/core.hpp"
#include "opencv2/calib3d/calib3d.hpp"
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/photo/photo.hpp"
#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

#define DEBUG_TAG "camera_intent"

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,DEBUG_TAG,__VA_ARGS__)

using namespace std;
using namespace cv;





extern "C" {
JNIEXPORT int JNICALL com_example_cameraintent_MainActivity_splitmpointojpg(JNIEnv *env, jobject,jstring);


JNIEXPORT int JNICALL Java_com_example_cameraintent_MainActivity_splitmpointojpg(JNIEnv *env, jobject,jstring path)
{

	/// Initialize Variables ..

	  char c;
	  int views = 0;  // number of views (JPG components)
	  long length;    // total length of file
	  size_t amount;  // amount read
	  char* buffer;
	//  char* fnm;
	  char* fnmbase;

	  LOGD("START");

	  const char *fnm = (env)->GetStringUTFChars(path, 0);

	  FILE* f = fopen(fnm,"rb");
	   if (f==NULL) {
	     LOGD("error opening file \"%s\"\n",fnm);
	     return 1;
	   }
	   // obtain file size:
	   fseek(f, 0, SEEK_END);
	   length = ftell(f);
	   rewind(f);


	   LOGD("2");
	   // allocate memory to contain the whole file:
	   //char buffer[BUFSIZ];
	   buffer = (char*) malloc (sizeof(char)*length);
	   if (buffer == NULL) {
	     LOGD("failed to allocate memory\n");
	     return 2;
	   } else {
	     LOGD("Allocated %ld chars of memory.\n",length);
	   }
	   amount = fread(buffer,1,length,f);
	   if (amount != length) {
	     LOGD("error loading file\n");
	     return 3;
	   }

	   LOGD("3");
	   fclose(f);
	   // NOW find the individual JPGs...
	   char* view = buffer;
	   char* last = NULL;
	   char* wnm = (char*) malloc(256);

	   //LOGD("Started at %p.\n",view);
	   while (view < buffer+length-4) {
		   LOGD("4");
	     if (((char) view[0] % 255) == (char) 0xff) {
	       if (((char) view[1] % 255) == (char) 0xd8) {
	         if (((char) view[2] % 255) == (char) 0xff) {
	           if (((char) view[3] % 255) == (char) 0xe1) {
	             LOGD( "View found at "+(view-buffer));
	             views++;
	             if (last != NULL) {
	               // copy out the previous view
	               sprintf(wnm, "%s.v%d.jpg", fnmbase, views-1);
	               FILE* w = fopen(wnm, "wb");
	               fwrite(last, 1, view-last, w);
	               fclose(w);
	               LOGD( "Created ");
	             }
	             last = view;
	             view+=4;
	           } else {
	             view+=2;
	           }
	         } else {
	           view+=3;
	         }
	       } else {
	         view+=1;
	       }
	     } else {
	       view+=1;
	     }
	   }
	   //LOGD("Stopped at %p.\n",view);
	   if (views > 1) {
	     // copy out the last view
	     sprintf(wnm, "%s.v%d.jpg", fnmbase, views);
	     FILE* w = fopen(wnm, "wb");
	     fwrite(last, 1, buffer+length-last, w);
	     fclose(w);
	     LOGD( "Created");
	   } else
	   if (views == 0) {
	     LOGD( "No views found.\n");
	   }
	   free(wnm);
	   free(buffer);

	   LOGD("test");

	return 10;

}
}

