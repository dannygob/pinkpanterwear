package com.example.pink.fragment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SmsGateway(
    private val baseUrl: String,
    private val partnerId: Int,
    private val apiKey: String,
    private val shortCode: String,
) {

    private fun getFinalURL(mobile: String?, message: String?): String {
        val encodedMessage = try {
            URLEncoder.encode(message.orEmpty(), StandardCharsets.UTF_8.toString())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            ""
        }

        val encodedMobile = try {
            URLEncoder.encode(mobile.orEmpty(), StandardCharsets.UTF_8.toString())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            ""
        }

        val finalUrl =
            "$baseUrl?apikey=$apiKey&partnerID=$partnerId&shortcode=$shortCode&mobile=$encodedMobile&message=$encodedMessage"
        println("📡 SMS Gateway URL: $finalUrl") // Debug log
        return finalUrl
    }

    suspend fun sendSingleSms(message: String?, mobile: String?): String {
        if (message.isNullOrBlank() || mobile.isNullOrBlank()) {
            return "ERROR: Mobile or message is blank"
        }
        return sendRequestAsync(getFinalURL(mobile, message))
    }

    suspend fun sendBulkSms(message: String?, mobiles: Array<String?>?): String {
        val numbers = mobiles?.filterNotNull()?.joinToString(",") ?: ""
        if (message.isNullOrBlank() || numbers.isBlank()) {
            return "ERROR: One or more mobile numbers are blank"
        }
        return sendRequestAsync(getFinalURL(numbers, message))
    }

    private suspend fun sendRequestAsync(urlString: String): String = withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.readTimeout = 15000

            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                reader.lineSequence().joinToString("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "ERROR: ${e.message ?: "Unknown exception"}"
        }
    }
}