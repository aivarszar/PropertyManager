package com.propertymanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propertymanager.models.Property
import com.propertymanager.models.Tenant
import com.propertymanager.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PropertyState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val selectedProperty: Property? = null,
    val tenants: List<Tenant> = emptyList(),
    val error: String? = null
)

class PropertyViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _propertyState = MutableStateFlow(PropertyState())
    val propertyState: StateFlow<PropertyState> = _propertyState

    fun loadProperties(ownerId: String) {
        viewModelScope.launch {
            _propertyState.value = _propertyState.value.copy(isLoading = true, error = null)

            repository.getProperties(ownerId).fold(
                onSuccess = { properties ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        properties = properties
                    )
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun loadProperty(propertyId: String) {
        viewModelScope.launch {
            _propertyState.value = _propertyState.value.copy(isLoading = true, error = null)

            repository.getProperty(propertyId).fold(
                onSuccess = { property ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        selectedProperty = property
                    )
                    // Load tenants for this property
                    if (property != null) {
                        loadTenants(propertyId)
                    }
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun createProperty(property: Property, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _propertyState.value = _propertyState.value.copy(isLoading = true, error = null)

            repository.createProperty(property).fold(
                onSuccess = { propertyId ->
                    _propertyState.value = _propertyState.value.copy(isLoading = false)
                    onSuccess(propertyId)
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun loadTenants(propertyId: String) {
        viewModelScope.launch {
            repository.getTenantsByProperty(propertyId).fold(
                onSuccess = { tenants ->
                    _propertyState.value = _propertyState.value.copy(tenants = tenants)
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(error = e.message)
                }
            )
        }
    }

    fun createTenant(tenant: Tenant, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _propertyState.value = _propertyState.value.copy(isLoading = true, error = null)

            repository.createTenant(tenant).fold(
                onSuccess = { tenantId ->
                    _propertyState.value = _propertyState.value.copy(isLoading = false)
                    // Reload tenants
                    loadTenants(tenant.propertyId)
                    onSuccess(tenantId)
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun registerTenantWithCode(code: String, userId: String, onSuccess: (Property) -> Unit) {
        viewModelScope.launch {
            _propertyState.value = _propertyState.value.copy(isLoading = true, error = null)

            repository.getTenantByInviteCode(code).fold(
                onSuccess = { tenant ->
                    if (tenant != null) {
                        val updatedTenant = tenant.copy(
                            userId = userId,
                            isRegistered = true
                        )
                        repository.updateTenant(updatedTenant).fold(
                            onSuccess = {
                                // Load the property
                                repository.getProperty(tenant.propertyId).fold(
                                    onSuccess = { property ->
                                        _propertyState.value = _propertyState.value.copy(isLoading = false)
                                        if (property != null) {
                                            onSuccess(property)
                                        }
                                    },
                                    onFailure = { e ->
                                        _propertyState.value = _propertyState.value.copy(
                                            isLoading = false,
                                            error = e.message
                                        )
                                    }
                                )
                            },
                            onFailure = { e ->
                                _propertyState.value = _propertyState.value.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        )
                    } else {
                        _propertyState.value = _propertyState.value.copy(
                            isLoading = false,
                            error = "Nederīgs kods"
                        )
                    }
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun deactivateTenant(tenant: Tenant) {
        viewModelScope.launch {
            val updatedTenant = tenant.copy(
                isActive = false,
                moveOutDate = System.currentTimeMillis()
            )
            repository.updateTenant(updatedTenant).fold(
                onSuccess = {
                    loadTenants(tenant.propertyId)
                },
                onFailure = { e ->
                    _propertyState.value = _propertyState.value.copy(error = e.message)
                }
            )
        }
    }

    fun clearError() {
        _propertyState.value = _propertyState.value.copy(error = null)
    }
}
