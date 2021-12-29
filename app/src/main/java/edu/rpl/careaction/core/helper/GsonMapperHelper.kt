package edu.rpl.careaction.core.helper

import com.google.gson.Gson

object GsonMapperHelper {
    private val gson = Gson()

    fun mapToJson(dto: Any) : String
        = gson.toJson(dto)

    fun<T> mapToDto(json: String?, kotlinClass: Class<T>) : T
        = gson.fromJson(json, kotlinClass)
}