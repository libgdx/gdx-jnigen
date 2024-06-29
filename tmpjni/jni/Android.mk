LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE    := test
LOCAL_C_INCLUDES := jni-headers jni-headers/ .
 
LOCAL_CFLAGS := $(LOCAL_C_INCLUDES:%=-I%) -O2 -Wall -D__ANDROID__
LOCAL_CPPFLAGS := $(LOCAL_C_INCLUDES:%=-I%) -O2 -Wall -D__ANDROID__
LOCAL_LDLIBS := -lm
LOCAL_ARM_MODE  := arm
 
LOCAL_SRC_FILES := C:\Users\tomco\Coding\tomski\gdx-jnigen\tmp\gdx-jnigen\android32\memcpy_wrap.c\
	C:\Users\tomco\Coding\tomski\gdx-jnigen\tmp\gdx-jnigen\android32\com_badlogic_gdx_jnigen_JniGenTestClass.cpp
 
include $(BUILD_SHARED_LIBRARY)
