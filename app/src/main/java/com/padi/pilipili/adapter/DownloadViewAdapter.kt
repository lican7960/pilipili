package com.padi.pilipili.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.padi.pilipili.R
import okhttp3.internal.concurrent.formatDuration

data class VideoItem(
    val cid: Long,
    val part: String,
    val duration: Int,
    val firstFrame: String?
)

class DownloadViewAdapter(
    private val items: List<VideoItem>,
    private val onItemClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<DownloadViewAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgThumb)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val duration: TextView = view.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_download_video_alert_list, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.title.text = it.part
        holder.duration.text = formatDuration(it.duration)

        if (!it.firstFrame.isNullOrEmpty()) {
            holder.img.load(it.firstFrame) {
                crossfade(true)
                placeholder(android.R.drawable.progress_indeterminate_horizontal)
                error(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.img.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener {
            onItemClick
        }
    }

    override fun getItemCount(): Int = items.size


    @SuppressLint("DefaultLocale")
    private fun formatDuration(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%d:%02d", m, s)
    }
}
