package com.boubyan.core.security

object SecurityConfig {
    const val ENABLE_CERTIFICATE_PINNING = true
    const val ENABLE_REQUEST_SIGNING = false
    const val API_KEY_HEADER = "X-API-Key"
    
    // Security headers
    val SECURITY_HEADERS = mapOf(
        "X-Content-Type-Options" to "nosniff",
        "X-Frame-Options" to "DENY",
        "X-XSS-Protection" to "1; mode=block"
    )
}