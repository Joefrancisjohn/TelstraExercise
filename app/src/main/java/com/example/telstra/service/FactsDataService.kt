package com.example.telstra.service

import com.example.telstra.model.FactsData

import retrofit2.http.GET
import retrofit2.Response


interface FactsDataService {
    @GET("facts.json/") //TODO FIX THIS
    suspend fun getAllFacts(): Response<FactsData>
}