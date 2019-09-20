package com.nekotoneko.redmoon.darkforestengine.consequences

import com.nekotoneko.redmoon.darkforestengine.keys.KeyStorage
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Silwèk on 2019-08-17
 */
class DefaultConsequenceExecutorTest {


    @Test
    fun execute_empty() {
        val keyStorage: KeyStorage = mock()
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(false, consequenceExecutor.execute(""))
    }

    @Test
    fun execute_set() {
        val keyStorage: KeyStorage = mock()
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        consequenceExecutor.execute("key=5")
        verify(keyStorage).setKey("key", "5")
    }

    @Test
    fun execute_setSuccess() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.setKey(eq("key"), any())).thenReturn(true)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(true, consequenceExecutor.execute("key=5"))
        verify(keyStorage).setKey("key", "5")
    }

    @Test
    fun execute_setFailed() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.setKey(eq("key"), any())).thenReturn(false)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(false, consequenceExecutor.execute("key=badValue"))
        verify(keyStorage).setKey("key", "badValue")
    }

    @Test
    fun execute_add() {
        val keyStorage: KeyStorage = mock()
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        consequenceExecutor.execute("key+5")
        verify(keyStorage).addToKeyValue("key", "5")
    }

    @Test
    fun execute_addSuccess() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.addToKeyValue(eq("key"), any())).thenReturn(true)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(true, consequenceExecutor.execute("key+5"))
        verify(keyStorage).addToKeyValue("key", "5")
    }

    @Test
    fun execute_addFailed() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.setKey(eq("key"), any())).thenReturn(false)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(false, consequenceExecutor.execute("key+badValue"))
        verify(keyStorage).addToKeyValue("key", "badValue")
    }

    @Test
    fun execute_remove() {
        val keyStorage: KeyStorage = mock()
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        consequenceExecutor.execute("key-5")
        verify(keyStorage).removeToKeyValue("key", "5")
    }

    @Test
    fun execute_removeSuccess() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.removeToKeyValue(eq("key"), any())).thenReturn(true)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(true, consequenceExecutor.execute("key-5"))
        verify(keyStorage).removeToKeyValue("key", "5")
    }

    @Test
    fun execute_removeFailed() {
        val keyStorage: KeyStorage = mock()
        whenever(keyStorage.setKey(eq("key"), any())).thenReturn(false)
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(false, consequenceExecutor.execute("key-badValue"))
        verify(keyStorage).removeToKeyValue("key", "badValue")
    }

    @Test
    fun execute_unknown() {
        val keyStorage: KeyStorage = mock()
        val consequenceExecutor = DefaultConsequenceExecutor(keyStorage)
        assertEquals(false, consequenceExecutor.execute("azerty"))
    }
}