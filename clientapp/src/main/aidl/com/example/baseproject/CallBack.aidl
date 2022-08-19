// CallBack.aidl
package com.example.baseproject;

// Declare any non-default types here with import statements

interface CallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void finish(String string);
    void waiting(String string);
}