package com.nekotoneko.redmoon.darkforestengine.sample.storyviewer

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.nekotoneko.redmoon.darkforestengine.sample.R
import com.nekotoneko.redmoon.darkforestengine.sample.models.Scene
import com.nekotoneko.redmoon.darkforestengine.sample.engine.EngineBuilder
import com.nekotoneko.redmoon.darkforestengine.sample.engine.StoryEngine
import kotlinx.android.synthetic.main.view_story.*

class StoryReaderActivity : AppCompatActivity(), StoryHandler.StoryAction {

    private var engine: StoryEngine? = null
    private var storyId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_reader)
        storyId = intent?.extras?.getString("storyId") ?: "storyA"
        title = intent?.extras?.getString("storyLabel") ?: storyId
    }

    override fun onStart() {
        super.onStart()
        val handler = StoryHandler(storyView)
        handler.storyAction = this
        storyView.visibility = VISIBLE
        storyView.onStoryLoad()
        engine = EngineBuilder().create(this, storyId, handler)
        engine?.scenarioEngine?.startScenario()
    }

    override fun next() {
        engine?.scenarioEngine?.next()
    }

    override fun choice(scene: Scene) {
        engine?.scenarioEngine?.next(scene)
    }

    override fun onEnd() {
        storyView.visibility = GONE
        finish()
    }
}
