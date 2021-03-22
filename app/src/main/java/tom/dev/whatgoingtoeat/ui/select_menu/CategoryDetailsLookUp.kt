package tom.dev.whatgoingtoeat.ui.select_menu

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class CategoryDetailsLookUp(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view) as CategoryListAdapter.CategoryListViewHolder
            return viewHolder.getItemDetails()
        }
        return null
    }
}