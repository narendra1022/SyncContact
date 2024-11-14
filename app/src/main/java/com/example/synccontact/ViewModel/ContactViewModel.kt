package com.example.synccontact.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synccontact.Model.Contact
import com.example.synccontact.Repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> get() = _uiState

    // Function to start syncing contacts
    fun startSync() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, isSyncSuccess = false)

            try {
                val contacts = contactRepository.getTodayContacts()
                contactRepository.syncContactsWithPhone(contacts)
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    contacts = contacts,
                    isSyncSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    contacts = emptyList(),
                    isSyncSuccess = false // Mark failure if there's an error
                )
                Log.e("ContactSync", "Error syncing contacts: ${e.message}")
            }
        }
    }
}


data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val isSyncing: Boolean = false,
    val isSyncSuccess: Boolean = false
)

