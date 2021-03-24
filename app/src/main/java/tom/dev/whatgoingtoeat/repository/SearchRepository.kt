package tom.dev.whatgoingtoeat.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.search.SearchDocument

interface SearchRepository {

    fun searchByKeyword(query: String, latitude: String, longitude: String): Flow<PagingData<SearchDocument>>
}