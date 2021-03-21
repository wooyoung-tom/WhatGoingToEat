package tom.dev.whatgoingtoeat.ui.select_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemSelectMenuBinding

class CategoryListAdapter(
    private val categoryList: List<String>,
    private val onCategoryItemClick: (String) -> Unit
): RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    inner class CategoryListViewHolder(private val binding: ItemSelectMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvItemSelectMenu.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            ItemSelectMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size
}