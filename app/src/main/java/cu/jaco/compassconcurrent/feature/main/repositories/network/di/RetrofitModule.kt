package cu.jaco.compassconcurrent.feature.main.repositories.network.di

import cu.jaco.compassconcurrent.feature.main.repositories.network.CompassService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class RetrofitModule {
    @Provides
    fun retrofitProvider(): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.compass.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Provides
    fun compassServiceProvider(retrofit: Retrofit): CompassService =
        retrofit.create(CompassService::class.java)
}