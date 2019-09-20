package com.nekotoneko.redmoon.darkforestengine.sample.stories

import android.content.res.AssetManager
import com.nekotoneko.redmoon.darkforestengine.sample.models.Story
import com.nekotoneko.redmoon.darkforestengine.sample.storyloader.StoryJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.FileNotFoundException

/**
 * @author Silwèk on 2019-08-14
 */
class StoriesRepository(private val assets: AssetManager) {
    fun getStories(
        onError: (() -> Unit)? = null,
        onLoad: (stories: List<Story>) -> Unit
    ) {
        //TODO Do it async
        val storyJsonContent = try {
            assets.open("stories/list.json").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            null
        }
        if (storyJsonContent.isNullOrBlank()) {
            onError?.invoke()
            return
        }
        val stories = convert(storyJsonContent)
        onLoad(stories ?: emptyList())
    }

    private fun convert(json: String): List<Story>? {
        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

        val listType = Types.newParameterizedType(List::class.java, StoryJson::class.java)
        val adapter: JsonAdapter<List<StoryJson>> = moshi.adapter(listType)
        val storiesJson = adapter.fromJson(json) ?: return null
        return storiesJson.map { convertStory(it) }.requireNoNulls()
    }

    private fun convertStory(json: StoryJson): Story? {
        return Story(id = json.id, label = json.label)
    }

}