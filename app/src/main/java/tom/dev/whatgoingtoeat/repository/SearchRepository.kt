package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.search.SearchResponse

interface SearchRepository {

    fun searchByKeyword(query: String, latitude: String, longitude: String, page: Int): Single<Response<SearchResponse>>
}