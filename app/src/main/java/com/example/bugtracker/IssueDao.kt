package com.example.bugtracker

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IssueDao {
    @Insert
    suspend fun insert(issue: IssueTicket): Long

    @Query("SELECT * FROM issues WHERE isDeleted = 0 ORDER BY creationDate DESC")
    fun getAllIssues(): Flow<List<IssueTicket>>

    @Update
    suspend fun update(issue: IssueTicket)

    @Query("UPDATE issues SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Long)

    @Query("SELECT * FROM issues WHERE isSynced = 0")
    suspend fun getUnsyncedIssues(): List<IssueTicket>

    @Query("DELETE FROM issues WHERE id = :id")
    suspend fun hardDelete(id: Long)
}

@Database(entities = [IssueTicket::class], version = 1)
abstract class BugTrackerDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao
}