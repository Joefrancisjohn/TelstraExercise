package com.example.telstra.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telstra.model.Repository

import com.example.telstra.repo.FactsRepo


class ListActivityViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ListActivityViewModel(
            repository) as T
    }
}
