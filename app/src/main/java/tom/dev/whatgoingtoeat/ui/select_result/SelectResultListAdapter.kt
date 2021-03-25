package tom.dev.whatgoingtoeat.ui.select_result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.ItemSelectResultBinding
import tom.dev.whatgoingtoeat.dto.history.HistoryCounter

class SelectResultListAdapter(
    private val categoryList: List<String>,
    private val categoryClickListener: (HistoryCounter) -> Unit
) : RecyclerView.Adapter<SelectResultListAdapter.SelectResultViewHolder>() {

    private val categoryResultList = ArrayList<HistoryCounter>()

    fun updateCategoryResultList(list: List<HistoryCounter>) {
        categoryResultList.clear()
        categoryResultList.addAll(list.toTypedArray())
    }

    fun findCategoryResultByName(name: String): HistoryCounter? {
        return categoryResultList.find {
            it.category == name
        }
    }

    inner class SelectResultViewHolder(private val binding: ItemSelectResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvItemSelectResultCategory.text = item

            val category = findCategoryResultByName(item)
            if (category == null) {
                // null 이라는 의미는 없다는 뜻
                binding.progressItemSelectResultCategory.progress = 0
                binding.tvItemSelectResultCount.text = "0명"
                binding.root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                // 있으니까 초기화하면 된다.
                binding.progressItemSelectResultCategory.progress = getProgress(category.selectedMemberCount, category.count)
                binding.tvItemSelectResultCount.text = getCountString(category.count)
                binding.root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.silver))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectResultViewHolder {
        return SelectResultViewHolder(
            ItemSelectResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelectResultViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    private fun getCountString(count: Long) = "${count}명"

    private fun getProgress(selectedCounter: Long, count: Long) = ((count.toDouble() / selectedCounter.toDouble()) * 100).toInt()
}