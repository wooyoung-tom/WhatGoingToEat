package tom.dev.whatgoingtoeat.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tom.dev.whatgoingtoeat.dto.search.SearchDocument
import tom.dev.whatgoingtoeat.service.SearchService
import java.io.IOException
import javax.inject.Inject

class SearchPagingSource
@Inject
constructor(
    private val searchService: SearchService,
    private val query: String,
    private val latitude: String,
    private val longitude: String
) : PagingSource<Int, SearchDocument>() {

    private val firstPage = 1

    override fun getRefreshKey(state: PagingState<Int, SearchDocument>): Int? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.toInt()
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchDocument> {
        val position = params.key ?: firstPage

        return try {
            val response = searchService.keywordSearch(query, latitude, longitude, position)
            LoadResult.Page(
                data = response.documents,
                prevKey = if (position == firstPage) null else position - 1,
                nextKey = if (response.meta.isEnd) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}