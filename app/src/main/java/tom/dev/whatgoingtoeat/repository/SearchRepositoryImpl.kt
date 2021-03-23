package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import retrofit2.Response
import tom.dev.whatgoingtoeat.dto.search.SearchResponse
import tom.dev.whatgoingtoeat.service.SearchService
import javax.inject.Inject

class SearchRepositoryImpl
@Inject
constructor(
    private val searchService: SearchService
) : SearchRepository {
    override fun searchByKeyword(query: String, latitude: String, longitude: String, page: Int): Single<Response<SearchResponse>> {
        return searchService.keywordSearch(query, latitude, longitude, page)
    }
}