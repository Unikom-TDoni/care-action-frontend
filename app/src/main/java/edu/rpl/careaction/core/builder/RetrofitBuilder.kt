package edu.rpl.careaction.core.builder

import com.google.gson.GsonBuilder
import edu.rpl.careaction.core.utility.DateUtility
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://care-action.000webhostapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setDateFormat(DateUtility.DATE_PATTERN_SERVER).create()
            )
        )
        .build()

    fun <T> buildService(service: Class<T>): T =
        retrofit.create(service)
}