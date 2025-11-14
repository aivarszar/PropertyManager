package com.propertymanager.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propertymanager.models.Payment
import com.propertymanager.models.PaymentStatus
import com.propertymanager.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PaymentState(
    val isLoading: Boolean = false,
    val payments: List<Payment> = emptyList(),
    val error: String? = null
)

class PaymentViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState: StateFlow<PaymentState> = _paymentState

    fun loadPaymentsByProperty(propertyId: String) {
        viewModelScope.launch {
            _paymentState.value = _paymentState.value.copy(isLoading = true, error = null)

            repository.getPaymentsByProperty(propertyId).fold(
                onSuccess = { payments ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        payments = payments
                    )
                },
                onFailure = { e ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun loadPaymentsByTenant(tenantId: String) {
        viewModelScope.launch {
            _paymentState.value = _paymentState.value.copy(isLoading = true, error = null)

            repository.getPaymentsByTenant(tenantId).fold(
                onSuccess = { payments ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        payments = payments
                    )
                },
                onFailure = { e ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun createPayment(payment: Payment, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _paymentState.value = _paymentState.value.copy(isLoading = true, error = null)

            repository.createPayment(payment).fold(
                onSuccess = {
                    _paymentState.value = _paymentState.value.copy(isLoading = false)
                    onSuccess()
                },
                onFailure = { e ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun uploadPaymentConfirmation(payment: Payment, imageUri: Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _paymentState.value = _paymentState.value.copy(isLoading = true, error = null)

            val path = "payment_confirmations/${payment.id}/${System.currentTimeMillis()}.jpg"
            repository.uploadImage(imageUri, path).fold(
                onSuccess = { imageUrl ->
                    val updatedPayment = payment.copy(
                        confirmationImageUrl = imageUrl,
                        status = PaymentStatus.AWAITING_CONFIRMATION
                    )
                    repository.updatePayment(updatedPayment).fold(
                        onSuccess = {
                            _paymentState.value = _paymentState.value.copy(isLoading = false)
                            onSuccess()
                        },
                        onFailure = { e ->
                            _paymentState.value = _paymentState.value.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    )
                },
                onFailure = { e ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun confirmPayment(payment: Payment, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _paymentState.value = _paymentState.value.copy(isLoading = true, error = null)

            val updatedPayment = payment.copy(
                status = PaymentStatus.CONFIRMED,
                confirmedAt = System.currentTimeMillis()
            )
            repository.updatePayment(updatedPayment).fold(
                onSuccess = {
                    _paymentState.value = _paymentState.value.copy(isLoading = false)

                    // If recurring, create next month's payment
                    if (payment.isRecurring) {
                        createNextMonthPayment(payment)
                    }

                    onSuccess()
                },
                onFailure = { e ->
                    _paymentState.value = _paymentState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    private fun createNextMonthPayment(payment: Payment) {
        viewModelScope.launch {
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = payment.dueDate
            calendar.add(java.util.Calendar.MONTH, 1)

            val nextPayment = payment.copy(
                id = "",
                dueDate = calendar.timeInMillis,
                status = PaymentStatus.PENDING,
                confirmationImageUrl = null,
                confirmedAt = null,
                createdAt = System.currentTimeMillis()
            )

            repository.createPayment(nextPayment)
        }
    }

    fun getPendingPaymentsCount(): Int {
        return _paymentState.value.payments.count { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.OVERDUE }
    }

    fun getAwaitingConfirmationCount(): Int {
        return _paymentState.value.payments.count { it.status == PaymentStatus.AWAITING_CONFIRMATION }
    }

    fun clearError() {
        _paymentState.value = _paymentState.value.copy(error = null)
    }
}
