package com.nekotoneko.redmoon.darkforestengine.sample.storysave

import android.util.Log
import com.nekotoneko.redmoon.darkforestengine.keys.Key
import com.nekotoneko.redmoon.darkforestengine.keys.KeyPositiveInt
import com.nekotoneko.redmoon.darkforestengine.keys.KeyStore


/**
 * @author Silwèk on 2019-08-14
 */
class EngineKeysStore(scenarioId: String, private val saveRepository: SaveRepository) : KeyStore(scenarioId) {

    var keys: List<Key> = listOf()
    var memory: Map<String, Key> = mapOf()
    var currentNodeId: String? = null

    override fun initKeys(keys: List<Key>) {
        this.keys = keys
        memory = ArrayList(keys).toList().associateBy({ it.id }, { it })
        currentNodeId = null
        retrieveProgress()
    }

    private fun retrieveProgress() {
        val save: MutableMap<String, Key> = HashMap()
        Log.d(TAG, "save for story $scenarioId : ")
        keys.forEach {
            it.setRawValue(get(it.id) ?: it.getDefaultRawValue())
            save[it.id] = it
            Log.d(TAG, "- ${it.id}->${it.getRawValue()} ")
        }
        memory = save.toMap()
        currentNodeId = saveRepository.getCurrentNode(scenarioId)
        Log.d(TAG, "- currentNodeId->$currentNodeId ")
    }

    override fun clearProgress() {
        Log.d(TAG, "clearProgress() for story $scenarioId")
        saveRepository.clearSaveData(scenarioId)
        retrieveProgress()
    }

    override fun getCurrentNode(): String? = currentNodeId

    override fun saveCurrentNode(nodeId: String?) {
        currentNodeId = nodeId
        saveRepository.saveCurrentNode(scenarioId, nodeId)
    }

    override fun setEnded() {
        saveRepository.setHasEnded(scenarioId)
    }

    override fun getProgress(): Map<String, Key> {
        return memory
    }

    override fun getKey(keyName: String): String? {
        if (memory.containsKey(keyName)) {
            return memory[keyName]?.getRawValue()
        }
        return null
    }

    override fun setKey(keyName: String, value: String): Boolean {
        if (memory.containsKey(keyName)) {
            val key = memory[keyName]
            key?.setRawValue(value)
            if (key != null) {
                put(key.id, key.getRawValue())
                return true
            }
        }
        return false
    }

    override fun addToKeyValue(keyName: String, value: String): Boolean {
        if (memory.containsKey(keyName)) {
            memory[keyName]?.let { key ->
                val baseValue = key.getRawValue() ?: key.getDefaultRawValue()
                key.setRawValue(addStringValue(baseValue, value, key is KeyPositiveInt))
                put(key.id, key.getRawValue())
                return true
            }
        }
        return false
    }

    override fun removeToKeyValue(keyName: String, value: String): Boolean {
        if (memory.containsKey(keyName)) {
            memory[keyName]?.let { key ->
                val baseValue = key.getRawValue() ?: key.getDefaultRawValue()
                key.setRawValue(removeStringValue(baseValue, value, key is KeyPositiveInt))
                put(key.id, key.getRawValue())
                return true
            }
        }
        return false
    }

    private fun put(key: String, value: String?) {
        saveRepository.put(scenarioId, key, value)
    }

    private fun get(key: String): String? = saveRepository.get(scenarioId, key)

    companion object {
        val TAG: String = EngineKeysStore::class.java.simpleName
    }
}