package com.boubyan.core.network

/**
 * Constants used for network operations.
 * Contains base URLs, parameter names, and timeout values.
 */
object NetworkConstants {
    /** Base URL for the NY Times API */
    const val BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/viewed/"
    /** Query parameter name for the API key */
    const val API_KEY_PARAM = "api-key"
    /** Connection timeout in seconds */
    const val CONNECT_TIMEOUT = 30L
    /** Read timeout in seconds */
    const val READ_TIMEOUT = 30L
    /** Write timeout in seconds */
    const val WRITE_TIMEOUT = 30L
}