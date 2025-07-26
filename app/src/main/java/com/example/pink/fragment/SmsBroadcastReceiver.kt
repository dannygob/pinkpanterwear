package com.example.pink.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {

    var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null

    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent)
        fun onFailure()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status

            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val consentIntent =
                        extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    if (consentIntent != null) {
                        smsBroadcastReceiverListener?.onSuccess(consentIntent)
                    } else {
                        smsBroadcastReceiverListener?.onFailure()
                    }
                }

                else -> {
                    smsBroadcastReceiverListener?.onFailure()
                }
            }
        }
    }
}