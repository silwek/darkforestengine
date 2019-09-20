package com.nekotoneko.redmoon.darkforestengine.consequences

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

/**
 * @author Silwèk on 2019-08-13
 */
class ConsequencesResolverTest {

    @Test
    fun resolve_empty() {
        val consequenceExecutor: ConsequenceExecutor = mock()
        val consequencesResolver = ConsequencesResolver()
        consequencesResolver.addExecutor(consequenceExecutor)
        consequencesResolver.resolve(" ")
        verify(consequenceExecutor, never()).execute(anyOrNull())
    }

    @Test
    fun resolve_simpleConsequence() {
        val consequenceExecutor: ConsequenceExecutor = mock()
        val consequencesResolver = ConsequencesResolver()
        consequencesResolver.addExecutor(consequenceExecutor)
        consequencesResolver.resolve("key=5")
        verify(consequenceExecutor).execute("key=5")
    }

    @Test
    fun resolve_multipleConsequence() {
        val consequenceExecutor: ConsequenceExecutor = mock()
        val consequencesResolver = ConsequencesResolver()
        consequencesResolver.addExecutor(consequenceExecutor)
        consequencesResolver.resolve("key=5;other+1")
        verify(consequenceExecutor).execute("key=5")
        verify(consequenceExecutor).execute("other+1")
    }
}