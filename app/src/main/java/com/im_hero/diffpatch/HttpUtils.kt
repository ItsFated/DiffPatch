package com.im_hero.diffpatch

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object HttpUtils {
    const val HOST_URL = "http://192.168.18.30:8080"
    const val GET_LATEST_VERSION = "/app/latest-version"
    const val GET_LATEST_PATCH = "/app/get-apk-patch"

    fun getLatestVersion(): JSONObject {
        val json: JSONObject
        val url = URL("$HOST_URL$GET_LATEST_VERSION")
        val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConn.doOutput = true
        urlConn.doInput = true
        val reader = BufferedReader(InputStreamReader(urlConn.inputStream))
        urlConn.connect()
        json = JSONObject(reader.readLine())
        reader.close()
        urlConn.disconnect()
        return json
    }
}