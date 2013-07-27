LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
OPENCV_LIB_TYPE:=STATIC
include /Users/rahulbudhiraja/Work/OpenCV_android/OpenCV-2.4.5-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := mposplit
LOCAL_SRC_FILES := mpo_split.cpp
LOCAL_LDLIBS +=  -L$(SYSROOT)/usr/lib -llog -ldl 

include $(BUILD_SHARED_LIBRARY)
