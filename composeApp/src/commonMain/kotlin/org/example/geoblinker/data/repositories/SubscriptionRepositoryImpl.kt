package org.example.geoblinker.data.repositories

import org.example.geoblinker.domain.models.PaymentInfo
import org.example.geoblinker.domain.models.PaymentResponseData
import org.example.geoblinker.domain.models.SubscriptionInfo
import org.example.geoblinker.domain.repositories.SubscriptionRepository
import org.example.geoblinker.domain.models.*
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.geoblinker.domain.repositories.ApiRepository

class SubscriptionRepositoryImpl(
    private val api: ApiRepository,
    private val settings: Settings,
    private val json: Json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
) : SubscriptionRepository {

    override suspend fun createSubscription(tariffId: String): Result<String> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")

            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }

            val subscriptionData = SubscriptionData(
                tariff = tariffId,
                autoRenew = 1
            )

            val dataJsonString = json.encodeToString(subscriptionData)

            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to dataJsonString
            )

            val response = api.createSubscription(request)

            if (response.code == "200") {
                println("SubscriptionRepo: Subscription created: ${response.data.subsId}")
                Result.success(response.data.subsId)
            } else {
                Result.failure(Exception("Failed to create subscription: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error creating subscription: $e")
            Result.failure(e)
        }
    }

    override suspend fun createPayment(
        amount: Int,
        subsId: String?,
        appUrl: String?
    ): Result<PaymentResponseData> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")

            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }

            val paymentData = PaymentData(
                sum = amount,
                currency = "RUB",
                paymentService = 1,
                subsId = subsId,
                paymentWay = 2
            )

            val dataJsonString = json.encodeToString(paymentData)

            val requestMap = mutableMapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to dataJsonString
            )

            appUrl?.let {
                println("SubscriptionRepo: Adding appUrl to payment request: $it")
                requestMap["appUrl"] = it
            }

            val response = api.createPayment(requestMap)

            if (response.code == "200") {
                println("SubscriptionRepo: Payment created: ${response.data.pId}")
                Result.success(response.data)
            } else {
                Result.failure(Exception("Failed to create payment: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error creating payment: $e")
            Result.failure(e)
        }
    }

    override suspend fun getPaymentStatus(paymentId: String): Result<PaymentInfo> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")

            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "p_id" to paymentId
            )

            val response = api.getPayment(request)

            if (response.code == "200" && response.data.payment.isNotEmpty()) {
                Result.success(response.data.payment[0])
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting payment status: $e")
            Result.failure(e)
        }
    }

    override suspend fun getUserSubscriptions(): Result<List<SubscriptionInfo>> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")

            val request = mapOf(
                "token" to token,
                "u_hash" to uHash
            )

            val response = api.getSubscription(request)

            if (response.code == "200") {
                Result.success(response.data.subscription ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get subscriptions: ${response.code}"))
            }

        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting subscriptions: $e")
            Result.failure(e)
        }
    }

    override suspend fun getTariffs(): Result<Map<String, Map<String, Any>>> = withContext(Dispatchers.Default) {
        try {
            println("SubscriptionRepo: Making getTariffs API call...")
            val response = api.getTariffs()
            println("SubscriptionRepo: getTariffs response code: ${response.code}")

            if (response.code == "200") {
                val tariffs = response.data.data.tariffs ?: emptyMap()
                println("SubscriptionRepo: Tariffs received: ${tariffs.size} items")
                Result.success(tariffs)
            } else {
                println("SubscriptionRepo: Failed to get tariffs: ${response.code}")
                Result.failure(Exception("Failed to get tariffs: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting tariffs: $e")
            Result.failure(e)
        }
    }

    companion object {
        const val PAYMENT_CREATED = 1
        const val PAYMENT_CANCELED = 3
        const val PAYMENT_SUCCEEDED = 6
    }
}
