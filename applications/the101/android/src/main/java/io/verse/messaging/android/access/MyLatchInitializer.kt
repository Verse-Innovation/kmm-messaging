package io.verse.messaging.android.access

import io.tagd.di.Scope
import io.tagd.langx.Context
import io.verse.latch.core.Latch
import io.verse.latch.core.LatchInitializer
import io.verse.latch.core.converter.gson.JsonCodecContentConverterFactory
import io.verse.latch.core.okhttp.OkHttpProtocolGateway
import io.verse.messaging.android.MyApplication

class MyLatchInitializer(app: MyApplication) : LatchInitializer<MyApplication>(
    app, app, "latch"
) {

    override fun initLatch(context: Context, outerScope: Scope, name: String?): Latch {
        return Latch.Builder()
            .name("messaging-latch")
            .context(context)
            .addBaseUrl("http", "http://demo2921399.mockable.io")
            .addBaseUrl("https", "https://demo2921399.mockable.io")
            .register("http", OkHttpProtocolGateway())
            .register("https", OkHttpProtocolGateway())
            .addPayloadConverterFactory(JsonCodecContentConverterFactory.new())
            .build()
    }
}