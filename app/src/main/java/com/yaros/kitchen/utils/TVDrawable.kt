package com.yaros.kitchen.utils

import android.R
import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat


class TVDrawable(val context: Context) {
    fun drawSize(tv: TextView, drawableInt: Int,size :Double, isLeft : Boolean = true){
        val drawable = ContextCompat.getDrawable(context, drawableInt)
        val pixelDrawableSize =
            Math.round(tv.lineHeight * size)
                .toInt() // Or the percentage you like (0.8, 0.9, etc.)

        drawable!!.setBounds(
            0,
            0,
            pixelDrawableSize,
            pixelDrawableSize
        ) // setBounds(int left, int top, int right, int bottom), in this case, drawable is a square image

        if (isLeft)
            tv.setCompoundDrawables(
                drawable,  //left
                null,  //top
                null,  //right
                null //bottom
            )

        else
            tv.setCompoundDrawables(
                null,  //left
                null,  //top
                drawable,  //right
                null //bottom
            )
    }


}