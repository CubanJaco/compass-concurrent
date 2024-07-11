package cu.jaco.compassconcurrent.utils

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> safeRunCatching(block: () -> R): Result<R> = runCatching(block).fold(
    onSuccess = { Result.success(it) },
    onFailure = {
        if (it is CancellationException) throw it
        Result.failure(it)
    }
)