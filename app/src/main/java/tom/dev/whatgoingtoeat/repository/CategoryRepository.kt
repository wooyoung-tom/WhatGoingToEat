package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.category.CategoryEntity

interface CategoryRepository {

    fun getAllCategories(): Single<List<CategoryEntity>>
}