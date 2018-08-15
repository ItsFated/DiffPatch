package com.im_hero.diffpatch

/**
 * Created on 2018/8/15.
 *
 * @author Jason
 * @version 1.0
 */
object BsDiff {

    init {
        System.loadLibrary("bsdiff")
    }

    external fun bsdiff(oldFile: String, newFile: String, patchFile: String): Int

    external fun bspatch(oldFile: String, newFile: String, patchFile: String): Int
}
