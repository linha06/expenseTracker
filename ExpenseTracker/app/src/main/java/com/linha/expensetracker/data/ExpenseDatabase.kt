package com.linha.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.linha.expensetracker.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense.db"
                ).fallbackToDestructiveMigration().build()

                fillWithStartingData(context, instance.expenseDao())

                INSTANCE = instance
                instance
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun fillWithStartingData(context: Context, dao: ExpenseDao) {
            val expense = loadJsonArray(context)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (expense != null) {
                        for (i in 0 until expense.length()) {
                            val item = expense.getJSONObject(i)
                            dao.insertAll(
                                Expense(
                                    item.getInt("id"),
                                    item.getString("title"),
                                    item.getInt("expense"),
                                    item.getLong("dueDate"),
                                    item.getInt("category")
                                )
                            )
                        }
                    }
                } catch (exception: JSONException) {
                    exception.printStackTrace()
                }
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.expense)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("moneys")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}