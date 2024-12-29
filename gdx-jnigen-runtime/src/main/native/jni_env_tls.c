#include "jni_env_tls.h"
#include <stdlib.h>
#ifdef _WIN32
#include <process.h>
#include <processthreadsapi.h>
#else
#include <pthread.h>
#endif

#ifdef __ANDROID__
#define THREAD_ATTACH_MACRO (*vm)->AttachCurrentThreadAsDaemon(vm, env, NULL);
#else
#define THREAD_ATTACH_MACRO (*vm)->AttachCurrentThreadAsDaemon(vm, (void**)env, NULL);
#endif

#define HANDLE_RESULT(res, msg) \
    if (res != JNI_OK) { \
        fprintf(stderr, msg ": %d", (int)res); \
        fflush(stderr); \
        exit(1); \
    }

#ifdef _WIN32
DWORD envTls = TLS_OUT_OF_INDEXES;

BOOL WINAPI DllMain(HINSTANCE hDLL, DWORD fdwReason, LPVOID lpvReserved) {
    // Docs say no cleanup when lpvReserved != NULL
    if (fdwReason == DLL_THREAD_DETACH && lpvReserved == NULL) {
        ThreadData* t_data = (ThreadData*)TlsGetValue(envTls);
        if (t_data != NULL)
            detach_jni_env(t_data);
    }

    return TRUE;
}

void init_tls() {
    if (envTls != TLS_OUT_OF_INDEXES) {
        HANDLE_RESULT(-1, "TLS got double initialized")
    }
    envTls = TlsAlloc();
    if (envTls == TLS_OUT_OF_INDEXES) {
        HANDLE_RESULT(-1, "Failed to allocate TLS")
    }
}

void cleanup_tls() {
    if (envTls != TLS_OUT_OF_INDEXES) {
        BOOL res = TlsFree(envTls);
        HANDLE_RESULT(res, "Failed to deallocate TLS")
    }
}

void set_tls(ThreadData* t_data) {
    if (envTls == TLS_OUT_OF_INDEXES)
        init_tls();
    BOOL res = TlsSetValue(envTls, (LPVOID)t_data);;
    HANDLE_RESULT(res, "Failed to set thread data")
}

#else
pthread_key_t envTlsKey = 0;

void init_tls() {
    HANDLE_RESULT(envTlsKey, "TLS got double initialized")
        
    int res = pthread_key_create(&envTlsKey, detach_jni_env);
    HANDLE_RESULT(res, "Failed to create pthread key")
}

void cleanup_tls() {
    if (envTlsKey != 0) {
        int res = pthread_key_delete(envTlsKey);
        HANDLE_RESULT(res, "Failed to delete pthread key")
        envTlsKey = 0;
    }
}

void set_tls(ThreadData* t_data) {
    if (envTlsKey == 0)
        init_tls();
    int p_res = pthread_setspecific(envTlsKey, t_data);
    HANDLE_RESULT(p_res, "Failed to set thread data")
}
#endif


void detach_jni_env(void* data) {
    ThreadData* t_data = data;
    JavaVM* vm = t_data->vm;

    jint res = (*vm)->DetachCurrentThread(vm);
    HANDLE_RESULT(res, "Failed to detach thread")

    free(t_data);
}

void tls_attach_jni_env(JavaVM* vm, JNIEnv** env) {
    jint attach_res = (*vm)->GetEnv(vm, (void**)env, JNI_VERSION_1_6);
    if (attach_res == JNI_EDETACHED) {
        jint res = THREAD_ATTACH_MACRO
        HANDLE_RESULT(res, "Failed to attach thread")
        ThreadData* t_data = malloc(sizeof(ThreadData));
        t_data->env = *env;
        t_data->vm = vm;
        
        set_tls(t_data);
    } else {
        HANDLE_RESULT(attach_res, "Failed to get thread")
    }
}