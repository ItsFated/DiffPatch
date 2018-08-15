//
// Created by tuili on 2018/8/15.
//
#include <android/log.h>
#ifndef DIFFPATCH_IM_HERO_LOG_H
#define DIFFPATCH_IM_HERO_LOG_H

#define LOG_TAG "NDK.LOG"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

#endif //DIFFPATCH_IM_HERO_LOG_H
