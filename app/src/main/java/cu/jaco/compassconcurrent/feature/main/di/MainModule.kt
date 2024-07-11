package cu.jaco.compassconcurrent.feature.main.di

import cu.jaco.compassconcurrent.feature.main.usecases.MainUseCase
import cu.jaco.compassconcurrent.feature.main.usecases.MainUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {
    @Binds
    abstract fun bindMainUseCase(
        mainUseCase: MainUseCaseImpl,
    ): MainUseCase
}