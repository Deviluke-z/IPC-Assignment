// AIDLRemoteService.aidl
package com.example.baseproject;

import com.example.baseproject.CallBack;
import com.example.baseproject.MyParcelable;
// Declare any non-default types here with import statements

interface AIDLRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void get();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    oneway void sendParcel(CallBack callback);
}