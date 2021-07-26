package com.example.crmapp.db.dto

import java.util.*

/**
 * postal_codes テーブル
 */
data class PostalCode(
    val id: Int,
    val postalCode: String,
    val prefecture: String,
    val prefecture_katakana: String,
    val city: String,
    val city_katakana: String,
    val address: String,
    val address_katakana: String,
    val update_at: Date,
    val status_code: Int
)
