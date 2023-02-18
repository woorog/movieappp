package com.example.myapplication.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)

    //10개단위 검색기록 구현
    @Query("SELECT * FROM history ORDER BY uid DESC LIMIT :count")
    fun getRecent(count: Int): List<History>
}