package cu.jaco.compassconcurrent.feature.main.repositories.network

import retrofit2.http.GET

interface CompassService {
    @GET("/about")
    suspend fun fetchAboutInfo(): String
}