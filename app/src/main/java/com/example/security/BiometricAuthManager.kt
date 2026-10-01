package com.example.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Robust Android BiometricPrompt API Manager for Financial Data Security.
 * Supports Biometric Authentication (Fingerprint, Face) with seamless Device Credential (PIN/Pattern) fallback.
 */
object BiometricAuthManager {

    enum class BiometricStatus(val readableName: String) {
        AVAILABLE("Hardware Enrolled & Ready"),
        DEVICE_CREDENTIAL_ONLY("Device Screen Lock (PIN/Pattern) Available"),
        NO_HARDWARE("No Biometric Hardware Detected"),
        HARDWARE_UNAVAILABLE("Biometric Hardware Currently Busy"),
        NONE_ENROLLED("No Biometric Credentials Enrolled"),
        SECURITY_UPDATE_REQUIRED("Security Patch Required"),
        UNSUPPORTED("Biometric Authentication Unsupported")
    }

    data class DeviceBiometricCapability(
        val status: BiometricStatus,
        val canAuthenticateBiometric: Boolean,
        val canAuthenticateCredential: Boolean,
        val statusSummary: String
    )

    /**
     * Checks if the device has biometric sensors and/or device credentials available.
     */
    fun checkBiometricStatus(context: Context): BiometricStatus {
        val biometricManager = BiometricManager.from(context)
        val bioResult = biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
        val credResult = biometricManager.canAuthenticate(DEVICE_CREDENTIAL)

        return when {
            bioResult == BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            credResult == BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.DEVICE_CREDENTIAL_ONLY
            bioResult == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            bioResult == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.HARDWARE_UNAVAILABLE
            bioResult == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NONE_ENROLLED
            bioResult == BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> BiometricStatus.SECURITY_UPDATE_REQUIRED
            else -> BiometricStatus.UNSUPPORTED
        }
    }

    /**
     * Returns detailed capability info including fallback credential availability.
     */
    fun getDeviceCapability(context: Context): DeviceBiometricCapability {
        val biometricManager = BiometricManager.from(context)
        val bioCanAuth = biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
        val credCanAuth = biometricManager.canAuthenticate(DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
        val status = checkBiometricStatus(context)

        val summary = when (status) {
            BiometricStatus.AVAILABLE -> "Fingerprint & Face sensors ready"
            BiometricStatus.DEVICE_CREDENTIAL_ONLY -> "Device PIN / Screen Lock available"
            BiometricStatus.NONE_ENROLLED -> "No fingerprint or face registered in system settings"
            BiometricStatus.NO_HARDWARE -> "Emulator / No physical biometric scanner"
            BiometricStatus.HARDWARE_UNAVAILABLE -> "Sensor hardware busy or initializing"
            else -> "Biometric hardware unavailable"
        }

        return DeviceBiometricCapability(
            status = status,
            canAuthenticateBiometric = bioCanAuth,
            canAuthenticateCredential = credCanAuth,
            statusSummary = summary
        )
    }

    /**
     * Prompts the user using the Android BiometricPrompt API with AuthenticationResult callback.
     */
    fun promptBiometricWithResult(
        activity: FragmentActivity,
        title: String = "SmartBank Biometric Vault",
        subtitle: String = "Touch fingerprint sensor or scan face to unlock financial assets",
        description: String = "Biometric authentication secures your accounts, transactions, and sensitive financial records.",
        negativeButtonText: String = "Use Master Passcode",
        allowDeviceCredential: Boolean = false,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit,
        onFailed: () -> Unit
    ) {
        try {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricManager = BiometricManager.from(activity)

            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }

            val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setConfirmationRequired(false)

            val credCanAuth = biometricManager.canAuthenticate(DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS

            if (allowDeviceCredential && credCanAuth) {
                // When DEVICE_CREDENTIAL is used, negativeButtonText MUST NOT be set
                promptInfoBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            } else {
                // Standard biometric prompt with custom negative button
                promptInfoBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
                promptInfoBuilder.setNegativeButtonText(negativeButtonText)
            }

            val promptInfo = promptInfoBuilder.build()
            val biometricPrompt = BiometricPrompt(activity, executor, callback)
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            onError(-1, e.message ?: "Failed to initialize BiometricPrompt")
        }
    }

    /**
     * Standard overload without AuthenticationResult argument.
     */
    fun promptBiometric(
        activity: FragmentActivity,
        title: String = "SmartBank Biometric Vault",
        subtitle: String = "Touch fingerprint sensor or scan face to unlock financial assets",
        description: String = "Biometric authentication secures your accounts, transactions, and sensitive financial records.",
        negativeButtonText: String = "Use Master Passcode",
        allowDeviceCredential: Boolean = false,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit,
        onFailed: () -> Unit
    ) {
        promptBiometricWithResult(
            activity = activity,
            title = title,
            subtitle = subtitle,
            description = description,
            negativeButtonText = negativeButtonText,
            allowDeviceCredential = allowDeviceCredential,
            onSuccess = { _ -> onSuccess() },
            onError = onError,
            onFailed = onFailed
        )
    }
}

