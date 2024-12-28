package com.example.journals;

import android.content.Context;

import com.cloudinary.android.MediaManager;

import java.util.Map;

public class MediaManagerProvider {
    private static boolean isInitialized = false;
     public static void initialize(Context context, Map config){
         if(!isInitialized){
             MediaManager.init(context,config);
             isInitialized = true;
         }
     }
}
