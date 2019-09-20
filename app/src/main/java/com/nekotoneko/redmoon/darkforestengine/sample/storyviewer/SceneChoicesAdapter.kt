package com.nekotoneko.redmoon.darkforestengine.sample.storyviewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nekotoneko.redmoon.darkforestengine.sample.R
import com.nekotoneko.redmoon.darkforestengine.sample.models.Scene

/**
 * @author Silwèk on 2019-08-17
 */
class SceneChoicesAdapter(context: Context) : RecyclerView.Adapter<SceneChoicesAdapter.SceneChoiceViewHolder>() {
    val choices: MutableList<Scene> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var clickListener: SceneChoiceClickListener? = null

    fun setItems(items: List<Scene>) {
        choices.clear()
        choices.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneChoiceViewHolder {
        return SceneChoiceViewHolder(inflater.inflate(R.layout.item_story_choice, parent, false))
    }

    override fun getItemCount(): Int {
        return choices.size
    }

    override fun onBindViewHolder(holder: SceneChoiceViewHolder, position: Int) {
        holder.display(choices[position], clickListener)
    }

    interface SceneChoiceClickListener {
        fun onChoice(sceneChoice: Scene)
    }

    class SceneChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun display(
            sceneChoice: Scene,
            clickListener: SceneChoiceClickListener?
        ) {
            itemView.findViewById<TextView>(R.id.text).text = sceneChoice.content?.textKey ?: ""
            itemView.findViewById<View>(R.id.text).setOnClickListener { clickListener?.onChoice(sceneChoice) }
        }
    }
}
