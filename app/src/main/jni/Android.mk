LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := fullcolorled
LOCAL_SRC_FILES := fullcolorled.c

include $(BUILD_SHARED_LIBRARY)