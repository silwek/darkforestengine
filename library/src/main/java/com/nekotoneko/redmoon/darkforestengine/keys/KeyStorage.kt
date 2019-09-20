package com.nekotoneko.redmoon.darkforestengine.keys

/**
 * @author Silwèk on 2019-08-13
 */
interface KeyStorage {

    fun getKey(keyName: String): String?

    fun setKey(keyName: String, value: String): Boolean

    fun addToKeyValue(keyName: String, value: String): Boolean

    fun removeToKeyValue(keyName: String, value: String): Boolean

}