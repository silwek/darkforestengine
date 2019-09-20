package com.nekotoneko.redmoon.darkforestengine.sample.storyloader

import android.content.res.AssetManager
import com.nekotoneko.redmoon.darkforestengine.sample.models.Scene
import com.nekotoneko.redmoon.darkforestengine.sample.models.SceneContent
import com.nekotoneko.redmoon.darkforestengine.sample.models.SceneGroup
import com.nekotoneko.redmoon.darkforestengine.sample.models.Story
import com.nekotoneko.redmoon.darkforestengine.keys.*
import com.nekotoneko.redmoon.darkforestengine.scenario.Node
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioLoader
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.FileNotFoundException

/**
 * @author Silwèk on 2019-08-14
 */
class StoryRepository(private val assets: AssetManager) : ScenarioLoader {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    }

    override fun loadScenario(
        scenarioId: String,
        onError: (() -> Unit)?,
        onLoad: (nodes: List<Node>, keys: List<Key>) -> Unit
    ) {
        //TODO Do it async
        val keysJsonContent = try {
            assets.open("stories/$scenarioId/keys.json").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            null
        }
        if (keysJsonContent.isNullOrBlank()) {
            onError?.invoke()
            return
        }

        val nodesJsonContent = try {
            assets.open("stories/$scenarioId/story.json").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            null
        }
        if (nodesJsonContent.isNullOrBlank()) {
            onError?.invoke()
            return
        }

        val story = convert(scenarioId, keysJsonContent, nodesJsonContent)
        onLoad(story?.scenes ?: emptyList(), story?.keys ?: emptyList())
    }

    private fun convert(storyId: String, keysJson: String, storyJson: String): Story? {
        val story = Story(storyId)
        story.keys = convertKeys(keysJson)
        story.scenes = convertScenes(storyJson)
        return story
    }

    private fun convertKeys(json: String): List<Key>? {
        val listType = Types.newParameterizedType(List::class.java, KeyJson::class.java)
        val adapter: JsonAdapter<List<KeyJson>> = moshi.adapter(listType)
        val listJson = adapter.fromJson(json) ?: return null
        return listJson.map { convertKey(it) }.requireNoNulls()
    }

    private fun convertKey(json: KeyJson): Key? {
        //TODO get other info
        return when (json.type) {
            "string" -> KeyString(id = json.id, defaultValue = "")
            "uint" -> KeyPositiveInt(id = json.id, defaultValue = 0)
            "int" -> KeyInt(id = json.id, defaultValue = 0)
            "boolean" -> KeyBoolean(id = json.id, defaultValue = false)
            else -> null
        }
    }

    private fun convertScenes(json: String): List<Scene>? {
        val listType = Types.newParameterizedType(List::class.java, SceneJson::class.java)
        val adapter: JsonAdapter<List<SceneJson>> = moshi.adapter(listType)
        val listJson = adapter.fromJson(json) ?: return null
        return listJson.map { convertScene(it) }.requireNoNulls()
    }

    private fun convertScene(json: SceneJson): Scene {
        val scene: Scene
        if (json.nodes.isNullOrEmpty()) {
            scene = Scene(id = "")
        } else {
            val nodes = json.nodes.map { convertScene(it) }
            scene = SceneGroup(id = "", nodes = nodes)
            scene.type = json.type ?: SceneGroup.USER_CHOICES
        }
        convertScene(json, scene)
        return scene

    }

    private fun convertScene(json: SceneJson, scene: Scene) {
        with(scene) {
            id = json.id
            nextId = json.nextId
            content = convertContent(json.content)
            conditions = json.conditions
            consequences = json.consequences
        }

    }

    private fun convertContent(json: SceneContentJson?): SceneContent? {
        if (json == null)
            return null
        return SceneContent(textKey = json.text)
    }
}