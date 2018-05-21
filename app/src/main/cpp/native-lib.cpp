#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_de_byte_1artist_luggage_1planer_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
