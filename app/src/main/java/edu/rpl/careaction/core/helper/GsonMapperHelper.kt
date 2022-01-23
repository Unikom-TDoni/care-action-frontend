package edu.rpl.careaction.core.helper

import java.io.Reader
import com.google.gson.Gson
import java.lang.reflect.Type

object GsonMapperHelper {
    private val gson = Gson()

    fun mapToJson(dto: Any) : String
        = gson.toJson(dto)

    fun<T> mapToDto(json: Reader?, kotlinClass: Class<T>) : T?
        = gson.fromJson(json, kotlinClass)

    fun<T> mapStringToDto(json: String?, kotlinClass: Class<T>) : T?
            = gson.fromJson(json, kotlinClass)

    fun<T> mapStringToDtoCollection(json: String?, type: Type) : T?
        = gson.fromJson(json, type)
}