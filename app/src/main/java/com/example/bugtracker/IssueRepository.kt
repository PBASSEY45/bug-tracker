package com.example.bugtracker

import kotlinx.coroutines.flow.Flow
import java.io.IOException
import retrofit2.HttpException

class IssueRepository(
    private val dao: IssueDao,
    private val api: IssueApiService
) {
    val issues: Flow<List<IssueTicket>> = dao.getAllIssues()

    suspend fun createIssue(issue: IssueTicket) = dao.insert(issue)

    suspend fun updateIssue(issue: IssueTicket) =
        dao.update(issue.copy(isSynced = false))

    suspend fun deleteIssue(id: Long) = dao.softDelete(id)

    suspend fun syncWithServer() {
        // Push local changes up
        dao.getUnsyncedIssues().forEach { local ->
            if (local.isDeleted) {
                api.deleteIssue(local.id)
                dao.hardDelete(local.id)
            } else {
                val saved = api.updateIssue(local.id, local)
                dao.update(saved.copy(isSynced = true))
            }
        }
        // Pull remote changes down
        api.getIssues().forEach { remote ->
            dao.update(remote.copy(isSynced = true))
        }
    }
}

sealed class SyncResult {
    data object Success : SyncResult()
    data object NetworkError : SyncResult()
    data class ServerError(val code: Int) : SyncResult()
}

suspend fun trySync(repository: IssueRepository): SyncResult {
    return try {
        repository.syncWithServer()
        SyncResult.Success
    } catch (e: IOException) {
        SyncResult.NetworkError   // offline or timeout: data stays queued
    } catch (e: HttpException) {
        SyncResult.ServerError(e.code())
    }
}