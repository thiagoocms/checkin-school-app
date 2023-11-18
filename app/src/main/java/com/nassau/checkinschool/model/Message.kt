package com.nassau.checkinschool.model

import java.io.Serializable

class Message(
    val timestamp: Long? = null,
    val status: String? = null,
    val error: String? = null,
    val exception: String? = null,
    val message: String? = null,
    val path: String? = null
): Serializable