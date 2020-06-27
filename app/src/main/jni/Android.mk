LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := 7segment
LOCAL_SRC_FILES := 7segment.c

include $(BUILD_SHARED_LIBRARY)