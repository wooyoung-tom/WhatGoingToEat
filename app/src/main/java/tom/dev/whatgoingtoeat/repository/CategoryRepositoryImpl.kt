package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.category.CategoryEntity
import tom.dev.whatgoingtoeat.service.CategoryService
import javax.inject.Inject

class CategoryRepositoryImpl
@Inject
constructor(
    private val categoryService: CategoryService
) : CategoryRepository {

    override fun getAllCategories(): Single<List<CategoryEntity>> {
        return categoryService.getAllCategories()
    }
}