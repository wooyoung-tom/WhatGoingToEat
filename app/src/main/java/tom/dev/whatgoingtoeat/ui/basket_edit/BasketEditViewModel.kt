package tom.dev.whatgoingtoeat.ui.basket_edit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.ui.restaurant_info.SelectedRestaurantMenu
import javax.inject.Inject

@HiltViewModel
class BasketEditViewModel
@Inject
constructor() : ViewModel() {

    val selectedMenuList = ArrayList<SelectedRestaurantMenu>()

    fun selectMenu(menu: RestaurantMenu) {
        val foundMenu = selectedMenuList.find { it.menu.id == menu.id }

        // 찾은 메뉴가 없으면 추가하고 카운터 1로 초기화.
        if (foundMenu == null) selectedMenuList.add(SelectedRestaurantMenu(menu, 1))
        else {
            // 찾은메뉴가 있으면 카운터를 하나 늘린다.
            val idx = selectedMenuList.indexOf(foundMenu)
            selectedMenuList[idx].count++
        }
    }

    fun removeMenu(menu: RestaurantMenu) {
        val foundMenu = selectedMenuList.find { it.menu.id == menu.id }

        // 찾은 메뉴가 존재하면
        if (foundMenu != null) {
            val idx = selectedMenuList.indexOf(foundMenu)
            if (selectedMenuList[idx].count == 1) {
                selectedMenuList.remove(foundMenu)
            } else selectedMenuList[idx].count--
        }
    }
}