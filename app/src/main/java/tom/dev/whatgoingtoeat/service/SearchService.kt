package tom.dev.whatgoingtoeat.service

import retrofit2.http.GET
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.search.SearchResponse

interface SearchService {

    @GET("/lunch/search")
    suspend fun keywordSearch(
        @Query("query") query: String,
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("page") page: Int
    ): SearchResponse
}