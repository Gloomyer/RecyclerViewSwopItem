#include <jni.h>
#include <string>
#include <iostream>
#include <fstream>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_gloomyer_ndkdemo1_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
