package com.nekotoneko.redmoon.darkforestengine.consequences

/**
 * @author Silwèk on 2019-08-13
 */
class ConsequencesResolver {

    private val consequencesExecutors: MutableList<ConsequenceExecutor> = emptyList<ConsequenceExecutor>().toMutableList()

    fun addExecutor(executor: ConsequenceExecutor){
        consequencesExecutors.add(executor)
    }

    fun resolve(consequencesString: String) {
        if (consequencesString.isBlank()) return
        val consequences = consequencesString.trim().split(DELIMITER)
        consequences.forEach {
            executeConsequence(it)
        }
    }

    private fun executeConsequence(consequenceString: String) {
        consequencesExecutors.forEach { executor ->
            if (executor.execute(consequenceString)) {
                return@forEach
            }
        }
    }

    companion object {
        private const val DELIMITER = ';'
    }
}