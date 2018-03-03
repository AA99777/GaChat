package com.example.rui.gachat.utils;

/**
 * Created by Rui on 2017/12/27.
 */

public class MsgTypeUtils {
   public static int msgType(String msgType){
       int m = 0;
       if (msgType.equals("txt")){
           m=0;
       }
       else if(
               msgType.equals("image")
               ){
           m=1;
       }
       else if(
               msgType.equals("audio")
               ){
           m=2;
       }
       else if(
               msgType.equals("video")
               ){
           m=3;
       }
       else if(
               msgType.equals("file")
               ){
           m=4;
       }

       return m;
   }
}
