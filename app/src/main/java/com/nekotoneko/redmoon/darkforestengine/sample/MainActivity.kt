package com.nekotoneko.redmoon.darkforestengine.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nekotoneko.redmoon.darkforestengine.sample.models.Story
import com.nekotoneko.redmoon.darkforestengine.sample.stories.StoriesAdapter
import com.nekotoneko.redmoon.darkforestengine.sample.stories.StoriesRepository
import com.nekotoneko.redmoon.darkforestengine.sample.storysave.SaveRepository
import com.nekotoneko.redmoon.darkforestengine.sample.storyviewer.StoryReaderActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StoriesAdapter.StoryClickListener {

    var storiesAdapter: StoriesAdapter? = null
    val saveRepository by lazy {
        SaveRepository(
            getSharedPreferences(
                SaveRepository.DEFAULT_PREFS,
                Context.MODE_PRIVATE
            )
        )
    }
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stories.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        storiesAdapter = StoriesAdapter(this)
        stories.adapter = storiesAdapter
        storiesAdapter?.clickListener = this
    }

    override fun onStart() {
        super.onStart()
        StoriesRepository(assets).getStories {
            storiesAdapter?.stories?.clear()
            storiesAdapter?.stories?.addAll(it)
            storiesAdapter?.notifyDataSetChanged()
        }
    }

    override fun onStoryClick(story: Story) {
        when {
            saveRepository.getIsEnded(story.id) -> {
                val builder = AlertDialog.Builder(this)
                builder.apply {
                    setMessage("Cette histoire est terminée. Voulez-vous la recommencer ?")
                    setPositiveButton(
                        "Recommencer depuis le début"
                    ) { _, _ ->
                        alertDialog?.dismiss()
                        alertDialog = null
                        saveRepository.clearSaveData(story.id)
                        startStory(story)
                    }
                    setNegativeButton(
                        "Annuler"
                    ) { _, _ ->
                        alertDialog?.dismiss()
                        alertDialog = null
                    }
                }
                val alertDialog: AlertDialog? = builder.create()
                alertDialog?.show()
            }
            saveRepository.hasSaveData(story.id) -> {
                val builder = AlertDialog.Builder(this)
                builder.apply {
                    setMessage("Cette histoire est en cours. Que voulez-vous faire ?")
                    setPositiveButton(
                        "Reprendre ma progression"
                    ) { _, _ ->
                        alertDialog?.dismiss()
                        alertDialog = null
                        startStory(story)
                    }
                    setNegativeButton(
                        "Recommencer depuis le début"
                    ) { _, _ ->
                        alertDialog?.dismiss()
                        alertDialog = null
                        saveRepository.clearSaveData(story.id)
                        startStory(story)
                    }
                }
                val alertDialog: AlertDialog? = builder.create()
                alertDialog?.show()
            }
            else -> startStory(story)
        }
    }

    private fun startStory(story: Story) {
        startActivity(Intent(this@MainActivity, StoryReaderActivity::class.java).apply {
            putExtra("storyId", story.id)
            putExtra("storyLabel", story.label)
        })
    }

}
