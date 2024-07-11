package cu.jaco.compassconcurrent.feature.main.repositories.local.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class CharImmutableListMoshiAdapter {
    @ToJson
    fun immutableListToJson(list: ImmutableList<Char>): List<Char> = list

    @FromJson
    fun immutableListFromJson(list: List<Char>): ImmutableList<Char> = list.toImmutableList()
}