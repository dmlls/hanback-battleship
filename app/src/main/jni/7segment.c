#include <com_diego_hanbackbattleship_BattleActivity.h>
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
/*
#include <termios.h>
#include <sys/mman.h>*/

/*
 * Class:     com_diego_hanbackbattleship_BattleActivity
 * Method:    SegmentControl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_diego_hanbackbattleship_BattleActivity_SegmentControl
  (JNIEnv *env, jobject thiz, jint data)
{

    int dev, ret;

    dev = open("/dev/fpga_segment", O_RDWR | O_SYNC);

    if (dev != -1) {
        ret = write(dev, &data, sizeof(int));
        close(dev);
    } else {
        perror("ERROR: cannot open the device");
        exit(1);
    }
    return 0;
}
