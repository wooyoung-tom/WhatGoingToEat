package tom.dev.whatgoingtoeat.ui.search_result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemSearchResultBinding
import tom.dev.whatgoingtoeat.databinding.ItemSearchResultHeaderBinding
import tom.dev.whatgoingtoeat.dto.search.SearchDocument

class SearchResultListAdapter(
    private val searchResultClickListener: (SearchDocument?) -> Unit
) : PagingDataAdapter<SearchDocument, RecyclerView.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<SearchDocument>() {
        override fun areItemsTheSame(oldItem: SearchDocument, newItem: SearchDocument) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SearchDocument, newItem: SearchDocument) = oldItem.id == newItem.id
    }

    inner class SearchResultHeaderViewHolder(val binding: ItemSearchResultHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchDocument?) {
            if (item != null) {
                binding.tvItemSearchResultHeaderCategory.text = item.categoryName
                binding.tvItemSearchResultHeaderName.text = item.placeName
                binding.tvItemSearchResultHeaderDoro.text = item.roadAddressName
                binding.tvItemSearchResultHeaderJibun.text = item.addressName
                binding.tvItemSearchResultHeaderDistance.text = item.distance
            }
        }
    }

    inner class SearchResultListViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchDocument?) {
            if (item != null) {
                binding.tvItemSearchResultCategory.text = item.categoryName
                binding.tvItemSearchResultName.text = item.placeName
                binding.tvItemSearchResultDoro.text = item.roadAddressName
                binding.tvItemSearchResultJibun.text = item.addressName
                binding.tvItemSearchResultDistance.text = item.distance
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> SearchResultHeaderViewHolder(
                ItemSearchResultHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> SearchResultListViewHolder(
                ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val randomPosition = (0 until itemCount).random()
        when (holder) {
            is SearchResultHeaderViewHolder -> {
                val currentItem = getItem(randomPosition)
                holder.bind(currentItem)
                holder.binding.cardviewItemSearchResultHeader.setOnClickListener {
                    searchResultClickListener(currentItem)
                }
            }
            is SearchResultListViewHolder -> {
                val currentItem = getItem(position)
                holder.bind(currentItem)
                holder.itemView.setOnClickListener {
                    searchResultClickListener(currentItem)
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1
}