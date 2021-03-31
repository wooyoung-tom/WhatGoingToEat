package tom.dev.whatgoingtoeat.ui.restaurant_info

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel
@Inject
constructor(

) : ViewModel() {

    val selectedMenuList = ArrayList<SelectedRestaurantMenu>()

}