package com.example.strikeinn.di

import com.example.strikeinn.network.F1Api
import com.example.strikeinn.network.F1ApiClient
import com.example.strikeinn.presentation.DataScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule: Module = module {

    singleOf(::F1ApiClient) bind F1Api::class

    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        coerceInputValues = true
                        explicitNulls = false
                    })
            }
            install(HttpCache)

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
            }

        }
    }

}

val viewModelModule: Module = module {
    viewModelOf(::DataScreenViewModel)
}