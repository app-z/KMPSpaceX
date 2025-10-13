package com.spacex.test

import com.spacex.database.AppDatabase
import com.spacex.repository.FalconRepository
import io.mockk.mockk
import org.junit.Test
import org.koin.dsl.module

class ComposeAppCommonTest {

//    single {
//        FalconRepository(get<AppDatabase>().falconDao())
//    }

    val testModule = module {
        single<FalconRepository> {
            mockk<FalconRepository>()
        } // Using Mockk for example
        // Add other mocked dependencies here
    }



    @Test
    fun example() {
//        assertEquals(3, 1 + 2)
    }
}