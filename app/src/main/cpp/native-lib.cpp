#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_de_byte_1artist_gepaeck_1planer_sybillesgepaeckplaner_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
