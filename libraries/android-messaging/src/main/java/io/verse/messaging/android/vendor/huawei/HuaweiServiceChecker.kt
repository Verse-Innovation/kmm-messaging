package io.verse.messaging.android.vendor.huawei

import android.os.Build
import android.util.Log
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.messaging.android.vendor.VendorServiceChecker

class HuaweiServiceChecker(
    private val weakContext: WeakReference<Context>,
) : VendorServiceChecker {

    private val context: Context?
        get() = weakContext.get()

    override fun canInitializeVendor(): Boolean {
        return context?.let { context ->
            isHuaweiDevice() && isHuaweiMobileServicesAvailable(context)
        } ?: kotlin.run {
            Log.e(TAG, "context must not be null")
            false
        }
    }

    private fun isHuaweiDevice(): Boolean {
        return Build.MANUFACTURER.equals("huawei", ignoreCase = false)
    }

    private fun isHuaweiMobileServicesAvailable(context: Context): Boolean {
        return HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS
    }

    companion object {
        private const val TAG = "HuaweiServiceChecker"
    }

}