// IMyAidlInterface.aidl
package com.study.lyds.aidlserver;

// Declare any non-default types here with import statements

interface IMyAidlInterface {

    int add(int a,int b);

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


}
