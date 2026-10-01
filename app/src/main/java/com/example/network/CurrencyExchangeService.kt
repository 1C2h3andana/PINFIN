package com.example.network

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

/**
 * Data response from Open Exchange Rates API (https://open.er-api.com/v6/latest/USD).
 */
@JsonClass(generateAdapter = true)
data class ExchangeRateResponse(
    @Json(name = "result") val result: String = "success",
    @Json(name = "provider") val provider: String = "Open Exchange Rates API",
    @Json(name = "base_code") val baseCode: String = "USD",
    @Json(name = "time_last_update_utc") val timeLastUpdateUtc: String = "",
    @Json(name = "rates") val rates: Map<String, Double> = emptyMap()
)

/**
 * Retrofit interface for fetching live foreign exchange rates.
 */
interface CurrencyExchangeApi {
    @GET("v6/latest/{base}")
    suspend fun getLatestRates(
        @Path("base") baseCurrency: String = "USD"
    ): Response<ExchangeRateResponse>
}

/**
 * Singleton Retrofit Client and Exchange Rate Utility.
 * Fetches real-time market rates and provides currency conversion functions.
 * Includes local cached fallbacks for offline reliability.
 */
object CurrencyExchangeClient {

    private const val TAG = "CurrencyExchangeClient"
    private const val BASE_URL = "https://open.er-api.com/"

    // Fallback baseline exchange rates against USD in case of network unavailability
    val FALLBACK_RATES: Map<String, Double> = mapOf(
        "USD" to 1.0,
        "EUR" to 0.921,
        "GBP" to 0.785,
        "JPY" to 153.45,
        "CAD" to 1.382,
        "AUD" to 1.518,
        "INR" to 83.82,
        "CHF" to 0.892,
        "CNY" to 7.245,
        "SGD" to 1.344,
        "AED" to 3.673,
        "BRL" to 5.485,
        "MXN" to 19.32,
        "NZD" to 1.632,
        "KRW" to 1342.50
    )

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val api: CurrencyExchangeApi by lazy {
        retrofit.create(CurrencyExchangeApi::class.java)
    }

    // In-memory cache of latest rates
    private var cachedRates: Map<String, Double> = FALLBACK_RATES
    private var lastUpdatedTime: String = "Live Fallback Rates"

    fun getCachedRates(): Map<String, Double> = cachedRates
    fun getLastUpdateTime(): String = lastUpdatedTime

    /**
     * Fetches live market exchange rates via Retrofit.
     * Updates cache and returns latest rates.
     */
    suspend fun fetchLiveRates(baseCurrency: String = "USD"): Pair<Map<String, Double>, String> {
        try {
            val response = api.getLatestRates(baseCurrency)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.rates.isNotEmpty()) {
                    cachedRates = body.rates
                    lastUpdatedTime = body.timeLastUpdateUtc.ifBlank { "Just Now" }
                    Log.d(TAG, "Successfully fetched ${cachedRates.size} live rates via Retrofit. Base: $baseCurrency")
                    return Pair(cachedRates, lastUpdatedTime)
                }
            } else {
                Log.w(TAG, "Retrofit request failed with code: ${response.code()}, message: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network error fetching exchange rates via Retrofit, using cached/fallback rates", e)
        }
        return Pair(cachedRates, lastUpdatedTime)
    }

    /**
     * Converts a given amount in foreign currency into the user's base account currency (default USD).
     * Formula: Amount in USD = Foreign Amount / Foreign Rate (since rates are expressed relative to 1 USD).
     */
    fun convertToBase(amount: Double, fromCurrency: String, baseCurrency: String = "USD"): Double {
        if (fromCurrency.equals(baseCurrency, ignoreCase = true)) return amount
        val rate = cachedRates[fromCurrency.uppercase()] ?: FALLBACK_RATES[fromCurrency.uppercase()] ?: 1.0
        return if (rate > 0.0) amount / rate else amount
    }

    /**
     * Converts an amount from base currency (USD) into a foreign currency.
     * Formula: Foreign Amount = USD Amount * Foreign Rate.
     */
    fun convertFromBase(amountInBase: Double, toCurrency: String, baseCurrency: String = "USD"): Double {
        if (toCurrency.equals(baseCurrency, ignoreCase = true)) return amountInBase
        val rate = cachedRates[toCurrency.uppercase()] ?: FALLBACK_RATES[toCurrency.uppercase()] ?: 1.0
        return amountInBase * rate
    }

    /**
     * Formats exchange rate quotation (e.g., 1 EUR = 1.085 USD).
     */
    fun getRateQuotation(foreignCurrency: String, baseCurrency: String = "USD"): String {
        val rate = cachedRates[foreignCurrency.uppercase()] ?: FALLBACK_RATES[foreignCurrency.uppercase()] ?: 1.0
        val oneUnitInBase = if (rate > 0.0) 1.0 / rate else 1.0
        return "1 ${foreignCurrency.uppercase()} = ${"%,.4f".format(oneUnitInBase)} $baseCurrency"
    }
}
