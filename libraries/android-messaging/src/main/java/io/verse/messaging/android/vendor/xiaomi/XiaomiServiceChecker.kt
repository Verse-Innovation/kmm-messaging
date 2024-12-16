package io.verse.messaging.android.vendor.xiaomi

import android.os.Build
import io.verse.messaging.android.vendor.VendorServiceChecker

class XiaomiServiceChecker: VendorServiceChecker {

    override fun canInitializeVendor(): Boolean {
        return Build.MANUFACTURER.equals("xiaomi", ignoreCase = true)
    }
}