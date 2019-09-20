package com.nekotoneko.redmoon.darkforestengine.sample.stories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nekotoneko.redmoon.darkforestengine.sample.R
import com.nekotoneko.redmoon.darkforestengine.sample.models.Story

/**
 * @author Silwèk on 2019-08-17
 */
class StoriesAdapter(context: Context) : RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>() {
    val stories: MutableList<Story> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var clickListener: StoryClickListener? = null

    fun setItems(items:List<Story>){
        stories.clear()
        stories.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(inflater.inflate(R.layout.item_story, parent, false))
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.display(stories[position], clickListener)
    }

    interface StoryClickListener {
        fun onStoryClick(story: Story)
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun display(
            story: Story,
            clickListener: StoryClickListener?
        ) {
            itemView.findViewById<TextView>(R.id.storyName).text = story.label ?: story.id
            itemView.findViewById<View>(R.id.storyName).setOnClickListener { clickListener?.onStoryClick(story) }
        }
    }
}
