@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.krest

import kong.unirest.*
import ru.inforion.lab403.common.json.parseJson
import ru.inforion.lab403.common.json.writeJson
import ru.inforion.lab403.common.logging.logger
import java.io.File

/**
 * Unirest Kotlin wrapper and request simplifier
 */
class Krest constructor(val url: String, val retries: Int = 10) {
    companion object {
        val log = logger()
    }

    constructor(
        host: String,
        port: Int,
        endpoint: String,
        protocol: String = "http",
        retries: Int = 10
    ) : this("$protocol://$host:$port/$endpoint", retries)

    inline fun <T> HttpResponse<T>.throwIfFailure() = apply {
        check(status == 200) { "Request to $url failed[${status} -> $statusText]: $body" }
    }

    inline fun <R> HttpRequest<*>.executeAs(retries: Int, action: () -> HttpResponse<R>): HttpResponse<R> {
        repeat(retries) { retry ->
            runCatching { action() }
                .onSuccess { return it }
                .onFailure { log.warning { "Request failed with '${it.message}' retry $retry/$retries" } }
        }
        error("Request $httpMethod $url failed within $retries retries")
    }

    inline fun <reified T : Any> HttpRequest<*>.executeAndGetResult(retries: Int): T =
        executeAs(retries) { asString() }.throwIfFailure().body.parseJson()

    inline fun HttpRequest<*>.executeAndGetFile(retries: Int, path: String): File =
        executeAs(retries) { asFile(path) }.throwIfFailure().body

    // :E method body doesn't set body ... it returns new request-object, eee... happy-debugging!
    // standard unirest serializer drops types id -> may required
    inline fun HttpRequestWithBody.json(body: Any?): HttpRequest<*> = if (body == null) this else body(body.writeJson())

    inline fun <T : HttpRequest<*>> T.headers(vararg headers: Pair<String, String?>) =
        apply { headers.filter { it.second != null }.forEach { header(it.first, it.second) } }

    inline fun <T : HttpRequest<*>> T.params(vararg params: Pair<String, String?>) =
        apply { params.filter { it.second != null }.forEach { queryString(it.first, it.second) } }

    inline fun HttpRequestWithBody.files(vararg files: Pair<String, File>): MultipartBody {
        require(files.isNotEmpty()) { "At least one file should be specified" }
        val first = files.first()
        var result = field(first.first, first.second)
        files.drop(1).forEach { result = result.field(it.first, it.second) }
        return result
    }

    inline fun <reified T : Any> post(endpoint: String, body: Any? = null, vararg headers: Pair<String, String?>): T =
        Unirest.post("$url/$endpoint").headers(*headers).json(body).executeAndGetResult(retries)

    inline fun <reified T : Any> delete(endpoint: String, body: Any? = null, vararg headers: Pair<String, String?>): T =
        Unirest.delete("$url/$endpoint").headers(*headers).json(body).executeAndGetResult(retries)

    inline fun <reified T : Any> get(endpoint: String, vararg headers: Pair<String, String?>): T =
        Unirest.get("$url/$endpoint").headers(*headers).executeAndGetResult(retries)

    inline fun file(endpoint: String, file: Pair<String, File>, vararg headers: Pair<String, String?>): Unit =
        Unirest.post("$url/$endpoint").headers(*headers).files(file).executeAndGetResult(retries)

    inline fun file(endpoint: String, path: String, vararg headers: Pair<String, String?>) =
        Unirest.get("$url/$endpoint").headers(*headers).executeAndGetFile(retries, path)
}