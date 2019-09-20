package com.nekotoneko.redmoon.darkforestengine.keys

/**
 * @author Silwèk on 2019-08-13
 */
abstract class KeyStore(val scenarioId: String) : KeyStorage {

    abstract fun initKeys(keys: List<Key>)

    abstract fun clearProgress()

    abstract fun getProgress(): Map<String, Key>

    abstract fun getCurrentNode(): String?

    abstract fun saveCurrentNode(nodeId: String?)

    abstract fun setEnded()

    fun addStringValue(value: String, add: String, alwaysPositive: Boolean): String {
        return try {
            val result = value.toInt() + add.toInt()
            if (alwaysPositive && result < 0)
                return 0.toString()
            result.toString()
        } catch (e: NumberFormatException) {
            value
        }
    }

    fun removeStringValue(value: String, remove: String, alwaysPositive: Boolean): String {
        return try {
            val result = value.toInt() - remove.toInt()
            if (alwaysPositive && result < 0)
                return 0.toString()
            result.toString()
        } catch (e: NumberFormatException) {
            value
        }
    }
}