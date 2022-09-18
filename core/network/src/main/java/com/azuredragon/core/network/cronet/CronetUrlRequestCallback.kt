package com.azuredragon.core.network.cronet

import android.os.ConditionVariable
import com.azuredragon.core.common.TAG
import com.azuredragon.core.log.Logger
import okhttp3.*
import okhttp3.EventListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.util.*

internal class CronetUrlRequestCallback(
    private val originalRequest: Request,
    private val call: Call,
    private val logger: Logger,
    private val eventListener: EventListener? = null,
    private val responseCallback: Callback? = null,
) : UrlRequest.Callback() {

    private var followCount = 0
    private var mException: IOException? = null

    private val mResponseCondition = ConditionVariable()
    private var mResponse = Response.Builder()
        .sentRequestAtMillis(System.currentTimeMillis())
        .request(originalRequest)
        .protocol(Protocol.HTTP_1_1)
        .code(0)
        .message("")
        .build()

    private val mBytesReceived = ByteArrayOutputStream()
    private val mReceiveChannel = Channels.newChannel(mBytesReceived)

    @Throws(IOException::class)
    fun waitForDone(): Response {
        mResponseCondition.block()
        if (mException != null) {
            throw mException as IOException
        }
        return mResponse
    }

    override fun onRedirectReceived(request: UrlRequest?, info: UrlResponseInfo?, newLocationUrl: String?) {
        if (followCount > MAX_FOLLOW_COUNT) {
            request?.cancel()
        }
        followCount += 1

        request?.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo) {
        mResponse = cronetToOkHttpResponse(mResponse, info, logger)

        eventListener?.responseHeadersEnd(call, mResponse)
        eventListener?.responseBodyStart(call)

        request?.read(ByteBuffer.allocateDirect(32 * 1024))
    }

    override fun onReadCompleted(request: UrlRequest?, info: UrlResponseInfo?, byteBuffer: ByteBuffer?) {
        // The response body is available, process byteBuffer.
        byteBuffer?.flip()
        mReceiveChannel.write(byteBuffer)

        // Continue reading the response body by reusing the same buffer
        // until the response has been completed.
        byteBuffer?.clear()
        request?.read(byteBuffer)
    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo) {
        // The request has completed successfully.
        eventListener?.responseBodyEnd(call, info.receivedByteCount)

        val contentTypeString = mResponse.header("content-type")
        val contentType = (contentTypeString ?: "text/plain; charset=\"utf-8\"").toMediaTypeOrNull()

        val responseBody = mBytesReceived.toByteArray().toResponseBody(contentType)
        val newRequest: Request = originalRequest.newBuilder().url(info.url).build()
        mResponse = mResponse.newBuilder().body(responseBody).request(newRequest).build()

        mResponseCondition.open()
        eventListener?.callEnd(call)
        responseCallback?.onResponse(call, mResponse)
    }

    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        val exception = if (error is org.chromium.net.NetworkException) {
            error
        } else {
            val errorMessage = "Cronet exception"

            val exception = IOException(errorMessage, error)
            logger.e(TAG, error?.message ?: errorMessage, exception)
            exception
        }

        mException = exception

        mResponseCondition.open()
        eventListener?.callFailed(call, exception)
        responseCallback?.onFailure(call, exception)
    }

    override fun onCanceled(request: UrlRequest?, info: UrlResponseInfo?) {
        mResponseCondition.open()
        eventListener?.callEnd(call)
    }


    companion object {
        private const val MAX_FOLLOW_COUNT = 20

        private fun cronetToOkHttpResponse(response: Response, responseInfo: UrlResponseInfo, logger: Logger): Response {
            val protocol = protocolFromNegotiatedProtocol(responseInfo)
            val headers = headersFromResponse(responseInfo, logger)
            return response.newBuilder()
                .receivedResponseAtMillis(System.currentTimeMillis())
                .protocol(protocol)
                .code(responseInfo.httpStatusCode)
                .message(responseInfo.httpStatusText)
                .headers(headers)
                .build()
        }

        private fun headersFromResponse(responseInfo: UrlResponseInfo, logger: Logger): Headers {
            val headers = responseInfo.allHeadersAsList
            val headerBuilder = Headers.Builder()
            for ((key, value) in headers) {
                try {
                    if (key.equals("content-encoding", ignoreCase = true)) {
                        // Strip all content encoding headers as decoding is done handled by cronet
                        continue
                    }
                    headerBuilder.add(key, value)
                } catch (e: Exception) {
                    logger.w(TAG, "Invalid HTTP header/value: $key, $value")
                    // Ignore that header
                }
            }
            return headerBuilder.build()
        }

        private fun protocolFromNegotiatedProtocol(responseInfo: UrlResponseInfo): Protocol {
            val negotiatedProtocol = responseInfo.negotiatedProtocol.lowercase(Locale.ENGLISH)
            return when {
                negotiatedProtocol.contains("quic") -> Protocol.QUIC
                negotiatedProtocol.contains("h2") -> Protocol.HTTP_2
                negotiatedProtocol.contains("1.1") -> Protocol.HTTP_1_1
                else -> Protocol.HTTP_1_1
            }
        }
    }
}