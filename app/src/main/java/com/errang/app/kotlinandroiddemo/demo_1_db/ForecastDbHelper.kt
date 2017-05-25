package com.errang.app.kotlinandroiddemo.demo_1_db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.errang.app.kotlinandroiddemo.KotlinApplication
import com.errang.app.kotlinandroiddemo.demo_1_db.table.CityTable
import com.errang.app.kotlinandroiddemo.demo_1_db.table.DayForecastTable
import org.jetbrains.anko.db.*

/**
 * 数据库简单示例
 * Created by zengpu on 2017/5/24.
 */
class ForecastDbHelper(context: Context = KotlinApplication.instance)
    : ManagedSQLiteOpenHelper(context, ForecastDbHelper.DB_NAME, null, ForecastDbHelper.DB_VERSION) {

    companion object {
        val DB_NAME = "forecast.db"
        val DB_VERSION = 1
        // 线程安全的lazy委托方式，只有用到的时候才初始化
        val instance: ForecastDbHelper by lazy { ForecastDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(DayForecastTable.NAME, true,
                DayForecastTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                DayForecastTable.DATE to INTEGER,
                DayForecastTable.DESCRIPTION to TEXT,
                DayForecastTable.HIGH to INTEGER,
                DayForecastTable.LOW to INTEGER,
                DayForecastTable.ICON_URL to TEXT,
                DayForecastTable.CITY_ID to INTEGER)

        db.createTable(CityTable.NAME, true,
                CityTable.ID to INTEGER + PRIMARY_KEY,
                CityTable.CITY to TEXT,
                CityTable.COUNTRY to TEXT)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(CityTable.NAME, true)
        db.dropTable(DayForecastTable.NAME, true)
        onCreate(db)
    }
}