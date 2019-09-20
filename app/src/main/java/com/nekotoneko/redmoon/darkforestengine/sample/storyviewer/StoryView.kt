package com.nekotoneko.redmoon.darkforestengine.sample.storyviewer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nekotoneko.redmoon.darkforestengine.sample.R
import com.nekotoneko.redmoon.darkforestengine.sample.models.Scene
import com.nekotoneko.redmoon.darkforestengine.sample.models.SceneGroup
import kotlinx.android.synthetic.main.view_story.view.*

/**
 * @author Silwèk on 2019-08-17
 */
class StoryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var storyActionListener: StoryActionListener? = null
    var choicesAdaper: SceneChoicesAdapter? = null
    var choicesList: RecyclerView? = null

    fun onStoryLoad() {
        progressBar.visibility = View.VISIBLE
        btNext.setOnClickListener(null)
        btNext.visibility = View.GONE
        choicesList = findViewById(R.id.choices)
        choicesList?.visibility = View.GONE
        choicesAdaper = SceneChoicesAdapter(context)
        choicesAdaper?.clickListener = object : SceneChoicesAdapter.SceneChoiceClickListener {
            override fun onChoice(sceneChoice: Scene) {
                storyActionListener?.onChoose(sceneChoice)
            }

        }
        choicesList?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        choicesList?.adapter = choicesAdaper
    }


    fun display(scene: Scene) {
        progressBar.visibility = GONE
        (storyContent as StoryContentView).display(scene.content)
        btNext.setOnClickListener {
            storyActionListener?.onNext()
        }
        btNext.visibility = View.VISIBLE
        choicesList?.visibility = View.GONE
        choicesAdaper?.setItems(emptyList())
    }

    fun display(scene: Scene, choices: List<Scene>) {
        progressBar.visibility = GONE

        if (choices.isEmpty()) {
            display(scene)
        } else if (scene is SceneGroup && scene.type == SceneGroup.STORY_CROSSROADS) {
            display(choices[0])
        } else {
            (storyContent as StoryContentView).display(scene.content)
            btNext.setOnClickListener(null)
            btNext.visibility = View.GONE

            choicesList?.visibility = View.VISIBLE
            choicesAdaper?.setItems(choices)

        }

    }

    interface StoryActionListener {
        fun onNext()
        fun onChoose(scene: Scene)
    }
}