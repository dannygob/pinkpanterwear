package com.example.slickkwear

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SmsGateway(
    private val mBaseUrl: String,
    private val mPartnerId: Int,
    private val mApiKey: String,
    private val mShortCode: String
) {
    private var connection: HttpURLConnection? = null


    private fun getFinalURL(mobile: String, message: String): String {
        var encodedMessage: String? = null
        try {
            encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        var encodedMobiles: String? = null
        try {
            encodedMobiles = URLEncoder.encode(mobile, StandardCharsets.UTF_8.toString())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return mBaseUrl + "?apikey=" + mApiKey + "&partnerID=" + mPartnerId + "&shortcode=" +
                mShortCode + "&mobile=" + encodedMobiles + "&message=" + encodedMessage
    }

    @Throws(IOException::class)
    fun sendSingleSms(message: String, mobile: String): String {
        val finalUrl = getFinalURL(mobile, message)

        return makeHttpGetRequest(finalUrl)
    }

    @Throws(IOException::class)
    fun sendBulkSms(message: String, mobiles: Array<String?>): String {
        val numbers = mobiles.contentToString().replace("[", "")
            .replace("]", "")
            .replace(" ", "")

        val finalUrl = getFinalURL(numbers, message)

        return makeHttpGetRequest(finalUrl)
    }


    @Throws(IOException::class)
    private fun makeHttpGetRequest(urlString: String): String {
        val url = makeURL(urlString)

        if (connection == null) {
            connection = url.openConnection() as HttpURLConnection

            connection!!.requestMethod = "GET"
            connection!!.readTimeout = 15000
        }

        val `in` = BufferedReader(
            InputStreamReader(
                connection!!.inputStream
            )
        )

        var line: String?

        val content = StringBuilder()

        while ((`in`.readLine().also { line = it }) != null) {
            content.append(line)
            content.append(System.lineSeparator())
        }

        return content.toString()
    }


    @Throws(MalformedURLException::class)
    private fun makeURL(urlString: String): URL {
        return URL(urlString)
    }
}