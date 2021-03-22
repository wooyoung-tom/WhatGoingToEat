package tom.dev.whatgoingtoeat.ui.select_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel
@Inject
constructor(

): ViewModel() {

    var currentSelectedCategory: String? = null

    fun completeSelectCategory() {

        Log.d("ViewModel", "Category: $currentSelectedCategory")
    }
}