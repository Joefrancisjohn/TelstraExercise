package com.example.telstra.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.telstra.model.FactsData
import com.example.telstra.model.Repository
import com.example.telstra.service.RetrofitInstance
import kotlinx.coroutines.*
import retrofit2.HttpException

class ListActivityViewModel(private val repo : Repository) : ViewModel() {

    //TODO Move to repo class later
    private val _factsToList: MutableLiveData<FactsData> = MutableLiveData()
    val factsToList: LiveData<FactsData> = _factsToList

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var infoText: MutableLiveData<String?> = MutableLiveData(null)

    fun getFactsFromRepo() {
        isLoading.value = true
        infoText.value = "Loading" //TODO Can move to strings

        val nwResponse = repo.getAllFacts()

        viewModelScope.launch {
            try {
                val result = nwResponse.await()
                if (result.isSuccessful) {
                    val factsOut = result.body()
                    infoText.value = null
                    _factsToList.postValue(factsOut)
                } else {
                    infoText.value = "Something went wrong ${result.code()}"
                    showError()
                }
            } catch (e: HttpException) {
                showError()
            } catch (e: Throwable) {
                showError()
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun showError() {
        viewModelScope.launch {
            infoText.value = "Ooops: Something else went wrong"
            isLoading.value = false
            _factsToList.postValue(null)
        }
    }
}