package me.cookie.abyssclient.utils.io

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object HttpClient {
    private const val DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.60"

    init {
        HttpURLConnection.setFollowRedirects(true)
    }

    private fun make(url: String, method: String, agent: String = DEFAULT_AGENT): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 2000 // 2 seconds until connect timeouts
        httpConnection.readTimeout = 10000 // 10 seconds until read timeouts

        httpConnection.setRequestProperty("User-Agent", agent)

        httpConnection.instanceFollowRedirects = true
        httpConnection.doOutput = true

        return httpConnection
    }

    fun request(url: String, method: String, agent: String = DEFAULT_AGENT): String {
        val connection = make(url, method, agent)

        return connection.inputStream.reader().readText()
    }

    fun requestStream(url: String, method: String = "GET", agent: String = DEFAULT_AGENT): InputStream {
        val connection = make(url, method, agent)

        return connection.inputStream
    }

    fun get(url: String) = request(url, "GET")

    fun download(url: String, file: File) = FileOutputStream(file).use { make(url, "GET").inputStream.copyTo(it) }

}