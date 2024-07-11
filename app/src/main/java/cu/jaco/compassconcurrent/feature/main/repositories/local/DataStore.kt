package cu.jaco.compassconcurrent.feature.main.repositories.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val USER_DATA_STORE_NAME = "user_data_store"

@Singleton
class DataStore @Inject constructor(
    @ApplicationContext context: Context,
    moshi: Moshi,
) {
    private val listAdapter: JsonAdapter<ImmutableList<Char>> = moshi.adapter(
        Types.newParameterizedType(ImmutableList::class.java, Char::class.javaObjectType)
    )

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        migrations = listOf(
            SharedPreferencesMigration(
                context,
                USER_DATA_STORE_NAME,
                setOf(
                    PreferencesKeys.WORDS_COUNT.name,
                    PreferencesKeys.EVERY_10TH_CHARACTER.name,
                )
            )
        ),
        produceFile = { context.preferencesDataStoreFile(USER_DATA_STORE_NAME) }
    )

    private object PreferencesKeys {
        val WORDS_COUNT = stringPreferencesKey("words_count")
        val EVERY_10TH_CHARACTER = stringPreferencesKey("every_10th_character")
    }

    val wordCount: Flow<Int> = dataStore.data
        .catchIOException()
        .map { preferences ->
            preferences[PreferencesKeys.WORDS_COUNT]?.toIntOrNull() ?: 0
        }

    suspend fun setWordCount(wordCount: Int) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.WORDS_COUNT] = wordCount.toString() }
    }

    val every10thCharacterList: Flow<ImmutableList<Char>> = dataStore.data
        .catchIOException()
        .map { preferences ->
            val json = preferences[PreferencesKeys.EVERY_10TH_CHARACTER] ?: return@map persistentListOf()
            listAdapter.fromJson(json) ?: persistentListOf()
        }

    suspend fun setEvery10thCharacterList(every10thCharacterList: ImmutableList<Char>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EVERY_10TH_CHARACTER] = listAdapter.toJson(every10thCharacterList)
        }
    }

    private fun Flow<Preferences>.catchIOException(): Flow<Preferences> = catch { _ -> emit(emptyPreferences()) }
}