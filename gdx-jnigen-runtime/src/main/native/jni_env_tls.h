#ifndef JNI_ENV_TLS_H_
#define JNI_ENV_TLS_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct ThreadData {
    JavaVM* vm;
    JNIEnv* env;
} ThreadData;

void init_tls();
void cleanup_tls();
void tls_attach_jni_env(JavaVM* vm, JNIEnv** env);
void detach_jni_env(void* data);

#ifdef __cplusplus
}
#endif
#endif // JNI_ENV_TLS_H_
