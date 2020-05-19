package com.example.telstra.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.telstra.service.FactsDataService
import com.example.telstra.service.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

open class Repository {
    private val service = RetrofitInstance.makeRetrofitService()
    open fun getAllFacts(): Deferred<Response<FactsData>> =
        CoroutineScope(Dispatchers.IO).async {
            service.getAllFacts()
        }
}
