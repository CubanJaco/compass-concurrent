package cu.jaco.compassconcurrent.feature.main.repositories.local.di

import com.squareup.moshi.Moshi
import cu.jaco.compassconcurrent.feature.main.repositories.local.adapters.CharImmutableListMoshiAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoshiModule {
    @Singleton
    @Provides
    fun moshiProvider(): Moshi = Moshi.Builder()
        .add(CharImmutableListMoshiAdapter())
        .build()
}