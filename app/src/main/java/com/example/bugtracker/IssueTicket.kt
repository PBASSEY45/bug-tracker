package com.example.bugtracker

import androidx.room.Entity
import androidx.room.PrimaryKey

// Hotfix: title must not be blank; validated before insert
@Entity(tableName = "issues")
data class IssueTicket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val priority: String,        // LOW, MEDIUM, HIGH, CRITICAL
    val status: String,          // OPEN, IN_PROGRESS, CLOSED
    val creationDate: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)