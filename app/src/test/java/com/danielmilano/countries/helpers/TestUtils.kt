package com.danielmilano.countries.helpers

import com.google.common.io.Resources
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class TestUtils {
    companion object {
        internal fun readJsonFile(filename: String): JsonElement {
            val inputStream = this.javaClass.classLoader!!.getResourceAsStream(filename)
            val s = readTextStream(inputStream)
            val gson = Gson()
            return gson.fromJson(s, JsonElement::class.java)
        }

        private fun readTextStream(inputStream: InputStream): String {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
                result.write(buffer, 0, bytesRead)
            }
            return result.toString("UTF-8")
        }

        internal fun toJsonObject(src: Any): JsonObject {
            val gson = Gson()
            val jsonString = gson.toJson(src)
            val jsonParser = JsonParser()

            return jsonParser.parse(jsonString) as JsonObject
        }

        internal fun getJson(path: String): String {
            val uri = Resources.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        }
    }
}

@ExperimentalStdlibApi
inline fun <reified T> Gson.fromJsonToObjectType(json: String): T =
    fromJson(json, typeOf<T>().javaType)