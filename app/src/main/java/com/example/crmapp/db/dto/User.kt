package com.example.crmapp.db.dto

import java.util.*

/**
 * users テーブル
 */
data class User(
    val id: Int? = null,
    val full_name: String,
    val postal_code: String,
    val prefecture: String,
    val city: String,
    val address: String,
    val house_name: String,
    val create_at: Date? = null,
    val update_at: Date? = null,
    val status_code: Int? = null
)
