package tom.dev.whatgoingtoeat.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.search.SearchDocument
import tom.dev.whatgoingtoeat.service.SearchService
import javax.inject.Inject

class SearchRepositoryImpl
@Inject
constructor(
    private val searchService: SearchService
) : SearchRepository {

    override fun searchByKeyword(query: String, latitude: String, longitude: String): Flow<PagingData<SearchDocument>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                SearchPagingSource(searchService, query, latitude, longitude)
            }
        ).flow
    }
}