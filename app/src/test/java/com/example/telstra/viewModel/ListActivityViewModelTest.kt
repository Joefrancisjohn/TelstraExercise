package com.example.telstra.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.telstra.model.FactsData
import com.example.telstra.model.Repository
import com.example.telstra.model.Row
import com.example.telstra.service.FactsDataService
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class ListActivityViewModelTest {

    private lateinit var factsResponse: Response<FactsData>
    private lateinit var viewModel: ListActivityViewModel

    private lateinit var isLoadingLiveData: LiveData<Boolean>

    private lateinit var infoTextLiveData: LiveData<String?>

    private val factsRows = listOf<Row>(
        Row("description1","imageHref1","title1"),
        Row("description1","imageHref1","title1")
    )
    private val factsData = FactsData(factsRows,"Test")
    //private val factsResponse = Response<FactsData>

    @Mock
    private lateinit var factListLiveData: LiveData<FactsData>

    @Mock
    private lateinit var service: FactsDataService

    @Spy
    private lateinit var repo: Repository


    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()


    @Before
    fun setUp() {
        //Mockito.`when`(repo.getAllFacts()).thenReturn(Deferred<Response<FactsData>>factsData)
        viewModel = ListActivityViewModel(repo)
        isLoadingLiveData = viewModel.isLoading
        infoTextLiveData = viewModel.infoText
        factListLiveData = viewModel.factsToList
        `when`(factListLiveData.value).thenReturn(factsData)
    }


    @Test
    fun loadFactsShouldShowAndHideLoading() = runBlocking {
        var isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertTrue(it) }
        viewModel.getFactsFromRepo()
        Mockito.verify(repo).getAllFacts()
        //`when`(repo.getAllFacts()).thenAnswer { /*throw Exception()*/ factsData }
        isLoading = isLoadingLiveData.value
        Assert.assertNotNull(isLoading)
        isLoading?.let { Assert.assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun isInfoTextShouldBeNonNullForExeceptions() = runBlocking {
        viewModel.getFactsFromRepo()
        var infoText = infoTextLiveData.value
        Assert.assertNotNull(infoText)
        infoText?.let { Assert.assertEquals("Loading",it) }
       // viewModel.getFactsFromRepo()
       // Mockito.verify(repo).getAllFacts()
        `when`(repo.getAllFacts()).thenAnswer { throw Exception() }
        infoText = infoTextLiveData.value
        Assert.assertNotNull(infoText)
        //Assert.

        return@runBlocking
    }

    @Test
    fun updateFactsWhenFetchDone() = runBlocking {

        viewModel.getFactsFromRepo()
        Mockito.verify(repo).getAllFacts()
        Assert.assertNotNull(factListLiveData)
        factListLiveData.value.let { Assert.assertEquals(factsData, it ) }

        return@runBlocking
    }

}