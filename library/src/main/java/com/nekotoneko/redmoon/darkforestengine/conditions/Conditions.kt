package com.nekotoneko.redmoon.darkforestengine.conditions

import com.nekotoneko.redmoon.darkforestengine.keys.Key
import com.nekotoneko.redmoon.darkforestengine.keys.KeyBoolean
import com.nekotoneko.redmoon.darkforestengine.keys.KeyInt
import com.nekotoneko.redmoon.darkforestengine.keys.KeyString

/**
 * @author Silwèk on 2019-08-12
 */
abstract class Condition(val keyName: String, val targetValue: String) {
    abstract fun perform(key: Key?): Boolean
}

class BooleanCondition(val value: Boolean) : Condition("", value.toString()) {
    override fun perform(key: Key?): Boolean = value
}

class GreaterThanCondition(keyName: String, targetValue: String) :
    Condition(keyName, targetValue) {
    override fun perform(key: Key?): Boolean {
        if (key == null || key !is KeyInt) {
            return false
        }
        val valueToTest = key.value ?: key.defaultValue
        return try {
            val targetValueInt = targetValue.toInt()
            valueToTest > targetValueInt
        } catch (e: NumberFormatException) {
            false
        }
    }

    companion object {
        val REGEX = "^([A-Za-z]\\w+)>([0-9]*)\$".toRegex()
    }
}

class LowerThanCondition(keyName: String, targetValue: String) :
    Condition(keyName, targetValue) {
    override fun perform(key: Key?): Boolean {
        if (key == null || key !is KeyInt) {
            return false
        }
        val valueToTest = key.value ?: key.defaultValue
        return try {
            val targetValueInt = targetValue.toInt()
            valueToTest < targetValueInt
        } catch (e: NumberFormatException) {
            false
        }
    }

    companion object {
        val REGEX = "^([A-Za-z]\\w+)<([0-9]*)\$".toRegex()
    }
}

class EqualsToCondition(keyName: String, targetValue: String) :
    Condition(keyName, targetValue) {
    override fun perform(key: Key?): Boolean {
        if (key == null) {
            return false
        }
        when (key) {
            is KeyString -> {
                val valueToTest = key.value ?: key.defaultValue
                return valueToTest == targetValue
            }
            is KeyInt -> {
                val valueToTest = key.value ?: key.defaultValue
                return try {
                    val targetValueInt = targetValue.toInt()
                    valueToTest == targetValueInt
                } catch (e: NumberFormatException) {
                    false
                }
            }
            is KeyBoolean -> {
                val valueToTest = key.value ?: key.defaultValue
                return valueToTest == targetValue.toBoolean()
            }
            else -> return false
        }
    }

    companion object {
        val REGEX = "^([A-Za-z]\\w+)=(.[^=]*)\$".toRegex()
    }
}