package com.example.pruebaalvic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pruebaalvic.api.ServiceApi
import com.example.pruebaalvic.model.Photo
import com.example.pruebaalvic.model.errorcontroller.Resource
import com.example.pruebaalvic.model.errorcontroller.Status
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class TestApi {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var serviceApi: ServiceApi

    @Before
    fun setUp(){
        serviceApi =  Mockito.mock(ServiceApi::class.java)
    }

    @Test
    fun loadPhotos() = runBlocking {

        val response = listOf<Photo>(
                Photo(4,1,"hola","www.servira.com","www.servira.com"),
                Photo(4,7,"hola1","www.notiene.com","www.notiene.com")
        )
        val resp = Resource(Status.SUCCESS, response, null)

        Mockito.`when`(serviceApi.loadPhotos())
                .then {
                    invocation ->
                    invocation.getArgument<(Resource<List<Photo>>) -> Unit>(1).invoke(resp)
                }

        Assert.assertTrue(resp.data?.get(0) == response.get(0))
    }
}