package com.anje.kelvin.aconting.domain.model

import com.anje.kelvin.aconting.util.AppConstants

/**
 * Enum class representing different payment methods available in the application.
 * Provides type safety and consistent handling of payment methods across the app.
 */
enum class PaymentMethod(
    val displayName: String,
    val requiresImmediateSettlement: Boolean = true,
    val allowsPartialPayment: Boolean = false
) {
    CASH(
        displayName = AppConstants.PaymentMethods.CASH,
        requiresImmediateSettlement = true,
        allowsPartialPayment = false
    ),
    CARD(
        displayName = AppConstants.PaymentMethods.CARD,
        requiresImmediateSettlement = true,
        allowsPartialPayment = false
    ),
    MOBILE_MONEY(
        displayName = AppConstants.PaymentMethods.MOBILE_MONEY,
        requiresImmediateSettlement = true,
        allowsPartialPayment = false
    ),
    BANK_TRANSFER(
        displayName = AppConstants.PaymentMethods.BANK_TRANSFER,
        requiresImmediateSettlement = true,
        allowsPartialPayment = false
    ),
    CREDIT(
        displayName = AppConstants.PaymentMethods.CREDIT,
        requiresImmediateSettlement = false,
        allowsPartialPayment = true
    );

    companion object {
        /**
         * Default payment method for new transactions
         */
        val DEFAULT = CASH

        /**
         * Get PaymentMethod by display name
         */
        fun fromDisplayName(displayName: String): PaymentMethod? {
            return values().find { it.displayName == displayName }
        }

        /**
         * Get all payment methods that require immediate settlement
         */
        fun getImmediateSettlementMethods(): List<PaymentMethod> {
            return values().filter { it.requiresImmediateSettlement }
        }

        /**
         * Get all payment methods that allow deferred payment
         */
        fun getDeferredPaymentMethods(): List<PaymentMethod> {
            return values().filter { !it.requiresImmediateSettlement }
        }

        /**
         * Get all payment methods as display names for UI
         */
        fun getAllDisplayNames(): List<String> {
            return values().map { it.displayName }
        }
    }
}