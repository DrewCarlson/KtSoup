/**
 * KtSoup
 * Copyright (C) 2023 Drew Carlson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ktsoup

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

private var httpClient: HttpClient? = null

private val acceptedContentTypes = listOf(
    ContentType.Text.Html,
    ContentType.Text.Xml,
    ContentType.Application.Xml,
    ContentType.Application.Xml_Dtd,
)

/**
 * Fetch and parse the HTML from [urlString].
 *
 * @see configureClient to configure the Ktor [HttpClient] to be used.
 * @see setClient to set a preconfigured ktor [HttpClient] to be used.
 * @throws IllegalStateException When the response content type does not represent an HTML document.
 * @throws IOException When the response indicates a status code >= 300 (i.e. an error).
 * @return The parsed HTML document as a [KtSoupDocument].
 */
public suspend fun KtSoupParser.parseRemote(urlString: String): KtSoupDocument {
    val response = getOrCreateClient().get(urlString)
    return if (response.status.isSuccess()) {
        val contentType = checkNotNull(response.contentType()) {
            "Response did not indicate content type"
        }
        val isAcceptedContentType = acceptedContentTypes.any {
            it.contentSubtype == contentType.contentSubtype &&
                it.contentType == contentType.contentType
        }
        check(isAcceptedContentType) {
            "Response indicated an unacceptable content type: $contentType"
        }
        parse(response.bodyAsText())
    } else {
        throw IOException("Failed to fetch content with status '${response.status}' for '$urlString' ")
    }
}

/**
 * Create and configure a new [HttpClient] to be used by [parseRemote].
 */
@Suppress("UnusedReceiverParameter")
public fun KtSoupParser.configureClient(block: HttpClientConfig<*>.() -> Unit) {
    httpClient = HttpClient(block)
}

/**
 * Set a preconfigured [HttpClient] to be used by [parseRemote].
 */
@Suppress("UnusedReceiverParameter")
public fun KtSoupParser.setClient(newHttpClient: HttpClient) {
    httpClient = newHttpClient
}

private fun getOrCreateClient(): HttpClient {
    return httpClient ?: HttpClient().also { httpClient = it }
}

@Suppress("UnusedReceiverParameter")
internal fun KtSoupParser.clearClient() {
    httpClient?.close()
    httpClient = null
}
