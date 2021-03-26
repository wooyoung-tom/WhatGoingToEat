package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.GET
import tom.dev.whatgoingtoeat.dto.category.CategoryEntity

interface CategoryService {

    @GET("/lunch/categories")
    fun getAllCategories(): Single<List<CategoryEntity>>
}