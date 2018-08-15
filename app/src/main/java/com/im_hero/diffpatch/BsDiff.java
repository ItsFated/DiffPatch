package com.im_hero.diffpatch;

/**
 * Created on 2018/8/15.
 *
 * @author Jason
 * @version 1.0
 */
public class BsDiff {

    static {
        System.loadLibrary("bsdiff");
    }

    public native static int bsdiff(String oldFile, String newFile, String patchFile);

    public native static int bspatch(String oldFile, String newFile, String patchFile);
}
