package com.boubyan.core.security

import okhttp3.CertificatePinner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificatePinner @Inject constructor() {
    
    fun getCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add("api.nytimes.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") // Replace with actual pin
            .build()
    }
}