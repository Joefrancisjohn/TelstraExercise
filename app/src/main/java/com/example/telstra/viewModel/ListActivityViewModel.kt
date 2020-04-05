package com.example.telstra.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.telstra.model.FactsData
import com.example.telstra.service.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListActivityViewModel(application: Application) : AndroidViewModel(application) {

    //TODO Move to repo class later
    private val _factsToList : MutableLiveData<FactsData> = MutableLiveData()
    val factsToList : LiveData<FactsData> = _factsToList

    var isLoading : MutableLiveData<Boolean> =  MutableLiveData(true)
    var infoText : MutableLiveData<String?> =  MutableLiveData(null)

    fun getFactsFromRepo(){
        isLoading.value=true
        infoText.value ="Loading" //TODO Can move to stirings
        val service = RetrofitInstance.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getAllFacts()
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val factsOut = response.body()
                            isLoading.value=false
                            infoText.value =null
                            _factsToList.postValue(factsOut)
                        } else {
                            // println("Error: ${response.code()}")
                            infoText.value ="Something went wrong ${response.code()}"
                        }
                    }
                    catch (e: HttpException) {
                        println("Ooops:  ${e.message}")
                    }
                    catch (e: Throwable) {
                        //println("Ooops: Something else went wrong")
                        //infoText.value = "Ooops: Something else went wrong"
                        showError()
                    }
                }
            } catch (e: Throwable) {
                println("JOO:  ${e.message}")
                showError()
            }
        }
    }

    private fun showError() {
        CoroutineScope(Dispatchers.Main).launch {
            infoText.value = "Ooops: Something else went wrong"
            isLoading.value=false
            _factsToList.postValue(null)
        }
    }

}