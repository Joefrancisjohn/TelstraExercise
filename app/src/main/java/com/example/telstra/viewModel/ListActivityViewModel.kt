package com.example.telstra.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.telstra.model.FactsData
import com.example.telstra.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListActivityViewModel(application: Application) : AndroidViewModel(application) {

    //TODO Move to repo class later
    private val _factsToList: MutableLiveData<FactsData> = MutableLiveData()
    val factsToList: LiveData<FactsData> = _factsToList

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var infoText: MutableLiveData<String?> = MutableLiveData(null)

    fun getFactsFromRepo() {
        isLoading.value = true
        infoText.value = "Loading" //TODO Can move to strings
        val service = RetrofitInstance.makeRetrofitService()

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = service.getAllFacts()
                    withContext(Dispatchers.Main) {
                        try {
                            if (response.isSuccessful) {
                                val factsOut = response.body()
                                isLoading.value = false
                                infoText.value = null
                                _factsToList.postValue(factsOut)
                            } else {
                                infoText.value = "Something went wrong ${response.code()}"
                            }
                        } catch (e: HttpException) {
                            showError()
                        } catch (e: Throwable) {
                            showError()
                        }
                    }
                }
            } catch (e: Throwable) {
                showError()
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