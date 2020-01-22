package com.yaros.kitchen.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yaros.kitchen.R

class DialogUtil {
   companion object{
       fun bottom(LayoutId: Int,context: Context): Dialog? {
           val dialog = Dialog(context)
           val window = dialog.window!!
           window.setLayout(
               LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.WRAP_CONTENT
           )
           window.setGravity(Gravity.BOTTOM)

           window.attributes.windowAnimations =  R.style.dialoganim
               //    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
           window.setBackgroundDrawableResource(android.R.color.transparent)
           window.requestFeature(Window.FEATURE_NO_TITLE)
           dialog.setContentView(LayoutId)
           dialog.setCancelable(true)
           return dialog
       }
       fun bottomConstraint(LayoutId: Int,context: Context): Dialog? {
           val dialog = Dialog(context)
           val window = dialog.window!!
           window.setLayout(
               RelativeLayout.LayoutParams.MATCH_PARENT,
               RelativeLayout.LayoutParams.WRAP_CONTENT
           )
           window.setGravity(Gravity.BOTTOM)

           window.attributes.windowAnimations =  R.style.dialoganim
               //    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
           window.setBackgroundDrawableResource(android.R.color.transparent)
           window.requestFeature(Window.FEATURE_NO_TITLE)
           dialog.setContentView(LayoutId)
           dialog.setCancelable(true)
           return dialog
       }

       fun bottomRelative(LayoutId: Int,context: Context): Dialog? {
           val dialog = Dialog(context)
           val window = dialog.window!!
           window.setLayout(
               ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT
           )
           window.setGravity(Gravity.BOTTOM)

           window.attributes.windowAnimations =  R.style.dialoganim
               //    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
           window.setBackgroundDrawableResource(android.R.color.transparent)
           window.requestFeature(Window.FEATURE_NO_TITLE)
           dialog.setContentView(LayoutId)
           dialog.setCancelable(true)
           return dialog
       }
   }
}