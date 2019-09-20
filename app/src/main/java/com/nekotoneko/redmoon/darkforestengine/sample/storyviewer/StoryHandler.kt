package com.nekotoneko.redmoon.darkforestengine.sample.storyviewer

import android.util.Log
import com.nekotoneko.redmoon.darkforestengine.sample.models.Scene
import com.nekotoneko.redmoon.darkforestengine.scenario.Node
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioViewer

/**
 * @author Silwèk on 2019-08-14
 */
class StoryHandler(
    val storyView: StoryView
) : ScenarioViewer {

    var storyAction: StoryAction? = null

    init {
        Log.v(TAG, "Start of story")
        storyView.storyActionListener = object : StoryView.StoryActionListener {
            override fun onNext() {
                storyAction?.next()
            }

            override fun onChoose(scene: Scene) {
                storyAction?.choice(scene)
            }

        }
    }

    override fun display(node: Node) {
        val scene = node as Scene
        Log.v(TAG, "${scene.content}")
        storyView.display(scene)
    }

    override fun display(node: Node, choices: List<Node>) {
        val scene = node as Scene
        val subScenes = choices as List<Scene>
        var message = "${scene.content} :"
        subScenes.forEach {
            message += "\n- ${it.content}"
        }
        Log.v(TAG, message)
        storyView.display(scene, subScenes)
    }

    override fun displayEnd() {
        Log.v(TAG, "End of story")
        storyAction?.onEnd()
    }

    companion object {
        const val TAG = "StoryHandler"
    }

    interface StoryAction {
        fun next()
        fun choice(scene: Scene)
        fun onEnd()
    }
}