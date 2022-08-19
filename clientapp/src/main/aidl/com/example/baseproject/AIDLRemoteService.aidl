// AIDLRemoteService.aidl
package com.example.baseproject;

import com.example.baseproject.CallBack;
// Declare any non-default types here with import statements

interface AIDLRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void get();

    oneway void sendParcel(CallBack callback);
}