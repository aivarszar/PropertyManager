package com.propertymanager.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propertymanager.models.Issue
import com.propertymanager.models.IssueStatus
import com.propertymanager.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class IssueState(
    val isLoading: Boolean = false,
    val issues: List<Issue> = emptyList(),
    val error: String? = null
)

class IssueViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _issueState = MutableStateFlow(IssueState())
    val issueState: StateFlow<IssueState> = _issueState

    fun loadIssuesByProperty(propertyId: String) {
        viewModelScope.launch {
            _issueState.value = _issueState.value.copy(isLoading = true, error = null)

            repository.getIssuesByProperty(propertyId).fold(
                onSuccess = { issues ->
                    _issueState.value = _issueState.value.copy(
                        isLoading = false,
                        issues = issues
                    )
                },
                onFailure = { e ->
                    _issueState.value = _issueState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun createIssue(issue: Issue, imageUris: List<Uri>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _issueState.value = _issueState.value.copy(isLoading = true, error = null)

            // Upload images first
            val imageUrls = mutableListOf<String>()
            imageUris.forEachIndexed { index, uri ->
                val path = "issue_images/${issue.propertyId}/${System.currentTimeMillis()}_$index.jpg"
                repository.uploadImage(uri, path).fold(
                    onSuccess = { url -> imageUrls.add(url) },
                    onFailure = { e ->
                        _issueState.value = _issueState.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                        return@launch
                    }
                )
            }

            val issueWithImages = issue.copy(imageUrls = imageUrls)

            repository.createIssue(issueWithImages).fold(
                onSuccess = {
                    _issueState.value = _issueState.value.copy(isLoading = false)
                    onSuccess()
                },
                onFailure = { e ->
                    _issueState.value = _issueState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun updateIssueStatus(issue: Issue, status: IssueStatus, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _issueState.value = _issueState.value.copy(isLoading = true, error = null)

            val updatedIssue = if (status == IssueStatus.RESOLVED) {
                issue.copy(status = status, resolvedAt = System.currentTimeMillis())
            } else {
                issue.copy(status = status)
            }

            repository.updateIssue(updatedIssue).fold(
                onSuccess = {
                    _issueState.value = _issueState.value.copy(isLoading = false)
                    onSuccess()
                },
                onFailure = { e ->
                    _issueState.value = _issueState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun getUnresolvedIssuesCount(): Int {
        return _issueState.value.issues.count { it.status != IssueStatus.RESOLVED }
    }

    fun clearError() {
        _issueState.value = _issueState.value.copy(error = null)
    }
}
