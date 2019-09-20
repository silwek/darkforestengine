package com.nekotoneko.redmoon.darkforestengine.sample.storysave

import android.content.SharedPreferences
import android.util.Log


/**
 * @author Silwèk on 2019-08-14
 */
class SaveRepository(private val sharedPrefs: SharedPreferences) {

    fun hasSaveData(scenarioId: String): Boolean {
        return getCurrentNode(scenarioId) !== null || !getIsEnded(scenarioId)
    }

    fun clearSaveData(scenarioId: String) {
        Log.d(TAG, "clearProgress() for story $scenarioId")
        with(sharedPrefs.edit()) {
            sharedPrefs.all
                .filter { it.key.startsWith("$scenarioId-") }
                .map { it.key }.forEach { keyName ->
                    remove(keyName)
                }
            apply()
        }
    }

    fun put(scenarioId: String, key: String, value: String?) {
        Log.d(TAG, "put($key->$value) for story $scenarioId")
        with(sharedPrefs.edit()) {
            putString("$scenarioId-$key", value)
            apply()
        }
    }

    fun get(scenarioId: String, key: String): String? {
        return sharedPrefs.getString("$scenarioId-$key", null)
    }

    fun saveCurrentNode(scenarioId: String, nodeId: String?) {
        put(scenarioId, KEY_CURRENTNODE, nodeId)
    }

    fun getCurrentNode(scenarioId: String): String? {
        return get(scenarioId, KEY_CURRENTNODE)
    }

    fun setHasEnded(scenarioId: String) {
        put(scenarioId, KEY_ISENDED, true.toString())
    }

    fun getIsEnded(scenarioId: String): Boolean {
        return get(scenarioId, KEY_ISENDED)?.toBoolean() ?: false
    }

    companion object {
        val TAG: String = SaveRepository::class.java.simpleName
        private const val KEY_CURRENTNODE = "Save_CurrentNode"
        private const val KEY_ISENDED = "Save_isEnded"
        const val DEFAULT_PREFS = "com.nekotoneko.redmoon.darkforestengine.sample.scenarioengine.EngineBuilder"
    }
}