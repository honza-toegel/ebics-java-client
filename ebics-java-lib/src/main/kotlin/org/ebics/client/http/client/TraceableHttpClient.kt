package org.ebics.client.http.client

import org.apache.http.impl.client.CloseableHttpClient
import org.ebics.client.api.trace.IBaseTraceSession
import org.ebics.client.api.trace.TraceCategory
import org.ebics.client.api.trace.TraceManager
import org.ebics.client.exception.EbicsException
import org.ebics.client.exception.HttpClientException
import org.ebics.client.http.client.request.HttpClientRequest
import org.ebics.client.io.ByteArrayContentFactory

class TraceableHttpClient(
    httpClient: CloseableHttpClient,
    configuration: HttpClientRequestConfiguration,
    configurationName: String = "default",
    val traceManager: TraceManager,
) : SimpleHttpClient(httpClient, configuration, configurationName), ITraceableHttpClient {

    override fun sendAndTrace(request: HttpClientRequest, traceSession: IBaseTraceSession): ByteArrayContentFactory {
        try {
            traceManager.trace(request.content, traceSession, request = true)
            val response = send(request)
            traceManager.trace(response, traceSession, request = false)
            return response
        } catch (e: HttpClientException) {
            //The request was not sent yet (network issues, connection, dns, ..)
            //Therefore we update trace of the request with an exception
            traceManager.updateLastTrace(traceSession, TraceCategory.RequestError, e)
            throw e
        } catch (e: EbicsException) {
            //The request was sent and we received some error response
            traceManager.traceException(e, traceSession)
            throw e
        }
    }
}