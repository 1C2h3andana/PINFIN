package com.example.domain.engine

import com.example.data.model.RiskEvaluation
import com.example.data.model.RiskLevel
import java.util.Calendar

object FraudDetectionEngine {

    private val SUSPICIOUS_KEYWORDS = listOf(
        "lottery", "win", "reward", "prize", "giftcard", "kyc", "update-kyc",
        "verify-account", "urgent", "apk", "claim", "free-crypto", "telegram",
        "double-money", "investment-bot", "refund-desk", "support-desk"
    )

    private val SUSPICIOUS_DOMAINS = listOf(
        ".xyz", ".top", ".club", ".click", ".buzz", ".support", ".cf", ".gq", ".ml", ".ga"
    )

    private val KNOWN_SCAM_PATTERNS = listOf(
        "sbi-rewards", "hdfc-points", "paytm-verification", "gpay-cashback",
        "phonepe-claim", "axis-kyc-update", "icici-alert", "urgent-transfer"
    )

    /**
     * Evaluates transaction risk score in real-time.
     * @param amount Transaction amount
     * @param recipient Recipient name or ID
     * @param upiOrHandle UPI or account number
     * @param currentBalance User's available account balance
     * @param isNewBeneficiary Whether this recipient was never sent to before
     * @param note Any memo / remarks attached to the transfer
     */
    fun evaluateTransaction(
        amount: Double,
        recipient: String,
        upiOrHandle: String,
        currentBalance: Double,
        isNewBeneficiary: Boolean = true,
        note: String = ""
    ): RiskEvaluation {
        var score = 5
        val flags = mutableListOf<String>()

        val lowerRecipient = recipient.lowercase()
        val lowerUpi = upiOrHandle.lowercase()
        val lowerNote = note.lowercase()
        val combinedText = "$lowerRecipient $lowerUpi $lowerNote"

        // 1. Scam Keyword / Phishing Handle Analysis (+35 to +60 score)
        for (kw in SUSPICIOUS_KEYWORDS) {
            if (combinedText.contains(kw)) {
                score += 35
                flags.add("Suspicious keyword detected: '$kw' (often associated with phishing or social engineering)")
                break
            }
        }

        // 2. High-Risk / Phishing domain patterns
        for (scam in KNOWN_SCAM_PATTERNS) {
            if (combinedText.contains(scam)) {
                score += 45
                flags.add("Blacklisted scam entity pattern matched: '$scam'")
                break
            }
        }

        // 3. Amount proportion vs Current Balance (+15 to +30 score)
        if (currentBalance > 0 && amount > currentBalance * 0.7) {
            score += 25
            flags.add("High capital outflow: Transfer exceeds 70% of total available balance")
        } else if (currentBalance > 0 && amount > currentBalance * 0.4) {
            score += 15
            flags.add("Elevated amount: Transfer exceeds 40% of available balance")
        }

        // 4. Large single volume threshold (> $2,500)
        if (amount >= 5000.0) {
            score += 30
            flags.add("Ultra-high value transaction (Threshold > $5,000)")
        } else if (amount >= 2000.0) {
            score += 18
            flags.add("High value transaction (Threshold > $2,000)")
        }

        // 5. Unverified / New Beneficiary (+10 score)
        if (isNewBeneficiary) {
            score += 10
            flags.add("First-time unverified beneficiary handle")
        }

        // 6. Suspicious Time of Day (Midnight to 4:30 AM heuristic)
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour in 1..4) {
            score += 15
            flags.add("Anomalous transaction time window (01:00 - 04:59 AM)")
        }

        // 7. UPI handle heuristics (e.g. numeric random UPI like 9876543210@xyz or strange suffix)
        if (lowerUpi.contains("@") && (lowerUpi.endsWith(".xyz") || lowerUpi.contains("free") || lowerUpi.contains("bot"))) {
            score += 30
            flags.add("Anomalous virtual payment address domain / gateway")
        }

        // Bound score
        score = score.coerceIn(0, 100)

        val level = when {
            score >= 75 -> RiskLevel.CRITICAL
            score >= 45 -> RiskLevel.HIGH
            score >= 25 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val shouldBlock = score >= 80
        val requires2Fa = score >= 30

        val recommendation = when {
            shouldBlock -> "CRITICAL RISK: Transaction blocked by AI Anti-Fraud Engine to prevent unauthorized drain. Contact 24/7 Security Desk or verify identity."
            requires2Fa -> "SECURITY ALERT: Moderate risk detected. Requires mandatory Two-Factor OTP & biometric confirmation."
            else -> "SAFE: Standard verified transaction parameters."
        }

        return RiskEvaluation(
            score = score,
            level = level,
            requires2Fa = requires2Fa,
            shouldBlock = shouldBlock,
            flags = if (flags.isEmpty()) listOf("No suspicious behavioral patterns detected") else flags,
            recommendation = recommendation
        )
    }

    /**
     * Inspects a suspicious URL, message, or QR/UPI link to protect the user from phishing.
     */
    fun inspectLinkOrHandle(input: String): RiskEvaluation {
        val lower = input.lowercase().trim()
        var score = 10
        val flags = mutableListOf<String>()

        if (lower.isBlank()) {
            return RiskEvaluation(
                score = 0,
                level = RiskLevel.LOW,
                requires2Fa = false,
                shouldBlock = false,
                flags = listOf("Empty input provided"),
                recommendation = "Enter a URL, UPI ID, or SMS text to scan for fraud."
            )
        }

        for (domain in SUSPICIOUS_DOMAINS) {
            if (lower.contains(domain)) {
                score += 40
                flags.add("High-risk top-level domain '$domain' commonly used for disposable phishing sites")
            }
        }

        for (kw in SUSPICIOUS_KEYWORDS) {
            if (lower.contains(kw)) {
                score += 30
                flags.add("Phishing or fraud keyword detected: '$kw'")
            }
        }

        for (pattern in KNOWN_SCAM_PATTERNS) {
            if (lower.contains(pattern)) {
                score += 50
                flags.add("Impersonation pattern detected: '$pattern' (spoofing trusted banking brand)")
            }
        }

        if (lower.contains("http://") && !lower.contains("https://")) {
            score += 25
            flags.add("Insecure unencrypted HTTP connection (Lacks SSL/TLS)")
        }

        if (lower.matches(Regex(".*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b.*"))) {
            score += 45
            flags.add("Direct IP address host detected instead of domain name (Severe phishing indicator)")
        }

        score = score.coerceIn(0, 100)

        val level = when {
            score >= 70 -> RiskLevel.CRITICAL
            score >= 40 -> RiskLevel.HIGH
            score >= 20 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val recommendation = when (level) {
            RiskLevel.CRITICAL -> "DANGER: HIGH PROBABILITY OF PHISHING SCAM! Do NOT open this link, enter passwords, or send money."
            RiskLevel.HIGH -> "WARNING: Suspicious characteristics found. Verify authenticity directly through official banking channels."
            RiskLevel.MEDIUM -> "CAUTION: Unverified URL/Handle. Proceed with extreme vigilance."
            RiskLevel.LOW -> "SAFE: No known scam or phishing heuristics identified."
        }

        return RiskEvaluation(
            score = score,
            level = level,
            requires2Fa = score > 30,
            shouldBlock = score >= 70,
            flags = if (flags.isEmpty()) listOf("No malicious patterns or phishing signatures matched") else flags,
            recommendation = recommendation
        )
    }

    fun inspectUrlOrHandle(input: String): RiskEvaluation = inspectLinkOrHandle(input)
    fun inspectScamLink(input: String): RiskEvaluation = inspectLinkOrHandle(input)
}
