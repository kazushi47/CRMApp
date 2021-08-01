package com.example.crmapp.db.dao

import com.example.crmapp.db.DBConnect
import com.example.crmapp.db.dto.PostalCode
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * postal_codes テーブルのDAO
 */
class PostalCodeDAO {

    /**
     * 郵便番号が存在するかどうか
     */
    fun isPostalCodeAvailable(postalCode: String): Boolean {
        var res: Boolean = false

        var con: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            con = DBConnect.getConnection()
            ps = con.prepareStatement("select * from postal_codes where postal_code = ?")
            ps.setString(1, postalCode)
            rs = ps.executeQuery()
            res = rs.next()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            con?.close()
            ps?.close()
            rs?.close()
        }

        return res
    }

    /**
     * 郵便番号から住所を検索
     */
    fun findByPostalCode(postalCode: String): List<PostalCode> {
        val res = ArrayList<PostalCode>()

        var con: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            con = DBConnect.getConnection()
            ps = con.prepareStatement("select * from postal_codes where postal_code = ?")
            ps.setString(1, postalCode)
            rs = ps.executeQuery()
            while (rs.next()) {
                res.add(PostalCode(rs.getInt("id"), rs.getString("postal_code"), rs.getString("prefecture"), rs.getString("prefecture_katakana"), rs.getString("city"), rs.getString("city_katakana"), rs.getString("address"), rs.getString("address_katakana"), rs.getDate("update_at"), rs.getInt("status_code")))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            con?.close()
            ps?.close()
            rs?.close()
        }

        return res
    }

    /**
     * 郵便番号と対応する市区町村名が存在するかどうか
     */
    fun isPostalCodeAndCityAvailable(postalCode: String, city: String): Boolean {
        var res: Boolean = false

        var con: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            con = DBConnect.getConnection()
            ps = con.prepareStatement("select * from postal_codes where postal_code = ? and city = ?")
            ps.setString(1, postalCode)
            ps.setString(2, city)
            rs = ps.executeQuery()
            res = rs.next()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            con?.close()
            ps?.close()
            rs?.close()
        }

        return res
    }
}