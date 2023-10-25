package mhha.sample.myframe

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mhha.sample.myframe.databinding.ItemImageBinding
import mhha.sample.myframe.databinding.ItemLoadMoreBinding

class ImageAdapter(private val itemClickListener: ItemClickListener ) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem
        }//override fun areItemsTheSame

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem
        }//override fun areContentsTheSame
    }//object : DiffUtil.ItemCallback<ImageItems>()
) {

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    } //override fun getItemCount(): Int

    override fun getItemViewType(position: Int): Int {
        return if(itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    } //override fun getItemViewType(position: Int): Int

    //class ImageAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }//ITEM_IMAGE ->
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }//while (viewType)

    }//override fun onCreateViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
            else -> {}
        }
    }//override fun onBindViewHolder

    interface ItemClickListener{
        fun onLoadMoreClick()
    }//interface ItemClickListener

    companion object{
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }//companion object

} //class ImageAdapter : ListAdapter<, RecyclerView.ViewHolder>

sealed class ImageItems {

    data class Image(
        val uri: Uri,
    ) : ImageItems()//data class Image

    object LoadMore : ImageItems()

} //sealed class ImageItems

class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: ImageItems.Image){
        binding.previewImageView.setImageURI(item.uri)
    }//fun bind(item: ImageItems.Image)
}//class IMageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(itemClickListener: ImageAdapter.ItemClickListener){
        itemView.setOnClickListener { itemClickListener.onLoadMoreClick()}
    }//fun bind()
}//class ItemMoreViewHolder(private val binding: ItemLoadMoreBinding): RecyclerView.ViewHolder(binding.root)