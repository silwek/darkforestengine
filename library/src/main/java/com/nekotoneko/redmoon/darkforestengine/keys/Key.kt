package com.nekotoneko.redmoon.darkforestengine.keys

import kotlin.math.max

/**
 * @author Silwèk on 2019-08-12
 */
abstract class Key(var id: String) {
    abstract fun setRawValue(newValue: String?)
    abstract fun getRawValue(): String?
    abstract fun getDefaultRawValue(): String
}

open class KeyString(id: String, var value: String? = null, val defaultValue: String) : Key(id) {
    override fun setRawValue(newValue: String?) {
        value = newValue
    }
    override fun getRawValue(): String? = value
    override fun getDefaultRawValue(): String = defaultValue
}

open class KeyInt(id: String, var value: Int? = null, val defaultValue: Int) : Key(id) {
    override fun setRawValue(newValue: String?) {
        if (newValue?.isNotBlank() == true) {
            try {
                value = newValue.toInt()
            } catch (e: NumberFormatException) {
                value = null
            }
        } else
            value = null
    }
    override fun getRawValue(): String? = if (value == null) null else value.toString()
    override fun getDefaultRawValue(): String = defaultValue.toString()
}

open class KeyPositiveInt(id: String, value: Int? = null, defaultValue: Int) : KeyInt(id,value, defaultValue) {
    override fun setRawValue(newValue: String?) {
        if (newValue?.isNotBlank() == true) {
            try {
                value = max(0, newValue.toInt())
            } catch (e: NumberFormatException) {
                value = null
            }
        } else
            value = null
    }
}

class KeyBoolean(id: String, var value: Boolean? = null, val defaultValue: Boolean) : Key(id) {
    override fun setRawValue(newValue: String?) {
        if (newValue?.isNotBlank() == true) {
            try {
                value = newValue.toBoolean()
            } catch (e: NumberFormatException) {
                value = null
            }
        } else
            value = null
    }
    override fun getRawValue(): String? = if (value == null) null else value.toString()
    override fun getDefaultRawValue(): String = defaultValue.toString()
}