package tom.dev.whatgoingtoeat.ui.select_result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemSelectResultBinding
import tom.dev.whatgoingtoeat.dto.history.HistoryCounter

class SelectResultListAdapter: ListAdapter<HistoryCounter, SelectResultListAdapter.SelectResultViewHolder>(Companion) {

    companion object: DiffUtil.ItemCallback<HistoryCounter>() {
        override fun areItemsTheSame(oldItem: HistoryCounter, newItem: HistoryCounter) = oldItem.category == newItem.category
        override fun areContentsTheSame(oldItem: HistoryCounter, newItem: HistoryCounter) = oldItem == newItem
    }

    inner class SelectResultViewHolder(private val binding: ItemSelectResultBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryCounter) {
            binding.tvItemSelectResultCategory.text = item.category
            binding.tvItemSelectResultCount.text = getCountString(item.count)
            binding.progressItemSelectResultCategory.progress = getProgress(item.selectedMemberCount, item.count)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectResultViewHolder {
        return SelectResultViewHolder(
            ItemSelectResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelectResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getCountString(count: Long) = "${count}명"

    private fun getProgress(selectedCounter: Long, count: Long) = ((count.toDouble() / selectedCounter.toDouble()) * 100).toInt()
}