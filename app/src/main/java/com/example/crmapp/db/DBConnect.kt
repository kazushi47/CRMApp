package com.example.crmapp.db

import java.sql.Connection
import java.sql.DriverManager

/**
 * DB接続用
 */
class DBConnect {

    companion object {
        /**
         * [Connection] を取得
         */
        fun getConnection(): Connection {
            return DriverManager.getConnection("jdbc:mariadb://10.0.2.2:3306/crm_schema", "root", "")
        }
    }
}