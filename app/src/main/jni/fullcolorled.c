#include "com_diego_hanbackbattleship_BattleActivity.h"
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/mman.h>
#include <errno.h>

#define FULL_LED1   9
#define FULL_LED2   8
#define FULL_LED3   7
#define FULL_LED4   6
#define ALL_LED     5

/*
 * Class:     com_diego_hanbackbattleship_BattleActivity
 * Method:    FLEDControl
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_com_diego_hanbackbattleship_BattleActivity_FLEDControl
  (JNIEnv *env, jobject thiz, jint led_num, jint r, jint g, jint b)
{
int fd, ret;
    char buf[3];
    fd = open("/dev/fpga_fullcolorled", O_WRONLY);
    if (fd < 0) {
        return -errno;
    }
    ret = (int) led_num;
    switch(ret) {
        case FULL_LED1:
            ioctl(fd, FULL_LED1);
            break;
        case FULL_LED2:
            ioctl(fd, FULL_LED2);
            break;
        case FULL_LED3:
            ioctl(fd, FULL_LED3);
            break;
        case FULL_LED4:
            ioctl(fd, FULL_LED4);
            break;
        case ALL_LED:
            ioctl(fd, ALL_LED);
            break;
    }
    buf[0] = r;
    buf[1] = g;
    buf[2] = b;

    write(fd, buf, 3);

    close(fd);
    return ret;
}