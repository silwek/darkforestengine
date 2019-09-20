package com.nekotoneko.redmoon.darkforestengine.consequences

import com.nekotoneko.redmoon.darkforestengine.keys.KeyStorage

/**
 * @author Silwèk on 2019-08-13
 */
interface ConsequenceExecutor {
    fun execute(consequenceString: String): Boolean
}

class DefaultConsequenceExecutor(var keyStorage: KeyStorage) : ConsequenceExecutor {

    override fun execute(consequenceString: String): Boolean {
        if (consequenceString.isBlank()) return false
        val consequence = consequenceString.trim()

        var pair: Pair<String, String>?

        pair = extract(consequence, REGEX_SET)
        if (pair != null)
            return setValue(pair.first, pair.second)
        pair = extract(consequence, REGEX_ADD)
        if (pair != null)
            return addToValue(pair.first, pair.second)
        pair = extract(consequence, REGEX_REMOVE)
        if (pair != null)
            return removeToValue(pair.first, pair.second)

        return false
    }

    private fun extract(consequenceString: String, regex: Regex): Pair<String, String>? {
        val consequence = consequenceString.trim()
        val matches = regex.matches(consequence)
        if (matches) {
            val matchResult = regex.find(consequence)
            if (matchResult?.groups?.size == 3) {
                val key: String? = matchResult.groups[1]?.value
                val value: String? = matchResult.groups[2]?.value
                if (key != null && value != null)
                    return Pair(key, value)
            }
        }
        return null
    }

    private fun setValue(keyName: String, value: String): Boolean {
        return keyStorage.setKey(keyName, value)
    }

    private fun addToValue(keyName: String, value: String): Boolean {
        return keyStorage.addToKeyValue(keyName, value)
    }

    private fun removeToValue(keyName: String, value: String): Boolean {
        return keyStorage.removeToKeyValue(keyName, value)
    }

    companion object {
        private val REGEX_SET = "^([A-Za-z]\\w+)=(.[^=]*)\$".toRegex()
        private val REGEX_ADD = "^([A-Za-z]\\w+)\\+(.[^+]*)\$".toRegex()
        private val REGEX_REMOVE = "^([A-Za-z]\\w+)-(.[^-]*)\$".toRegex()
    }
}