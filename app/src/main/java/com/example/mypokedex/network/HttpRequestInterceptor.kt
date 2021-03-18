package com.example.mypokedex.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HttpRequestInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val r = chain.request()
        val req = r.newBuilder().url(r.url).build()
        Timber.d(req.toString())
        return chain.proceed(req)
    }
}
