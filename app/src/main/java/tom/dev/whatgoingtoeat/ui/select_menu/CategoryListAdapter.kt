package tom.dev.whatgoingtoeat.ui.select_menu

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.ItemSelectMenuBinding

class CategoryListAdapter(
    private val categoryList: List<String>,
    private val selectedItemController: (String?) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    init {
        setHasStableIds(true)
        notifyDataSetChanged()
    }

    private var selectionTracker: SelectionTracker<Long>? = null

    inner class CategoryListViewHolder(private val binding: ItemSelectMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvItemSelectMenu.text = item
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = itemId
                override fun inSelectionHotspot(e: MotionEvent): Boolean = true
            }
        }

        fun setSelectionTracker(selectionTracker: SelectionTracker<Long>?) {
            if (selectionTracker != null && selectionTracker.isSelected(adapterPosition.toLong())) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.apricot_alpha_80))
                binding.tvItemSelectMenu.setTextColor(ContextCompat.getColor(binding.root.context, R.color.energy_yellow))
            } else {
                binding.root.setCardBackgroundColor(Color.TRANSPARENT)
                binding.tvItemSelectMenu.setTextColor(ContextCompat.getColor(binding.root.context, R.color.dusty_gray))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            ItemSelectMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(categoryList[position])
        holder.setSelectionTracker(selectionTracker)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun getItemId(position: Int): Long = position.toLong()

    fun setSelectionTracker(selectionTracker: SelectionTracker<Long>) {
        this.selectionTracker = selectionTracker.apply {
            addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    super.onItemStateChanged(key, selected)

                    // Selection 이 하나일 때
                    if (selectionTracker.selection.size() == 1) {
                        if (selected) selectedItemController(categoryList[key.toInt()])
                        else selectedItemController(null)
                    } else {
                        selectionTracker.selection.forEach {
                            // 골라진 Selection Item 이라면
                            if (selected) selectedItemController(categoryList[key.toInt()])
                        }
                    }
                }
            })
        }
    }
}