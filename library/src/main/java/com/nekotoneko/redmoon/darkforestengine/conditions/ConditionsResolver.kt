package com.nekotoneko.redmoon.darkforestengine.conditions

import com.nekotoneko.redmoon.darkforestengine.keys.Key

/**
 * @author Silwèk on 2019-08-12
 */
class ConditionsResolver {

    private val conditionResolvers: MutableList<ConditionResolver> = emptyList<ConditionResolver>().toMutableList()

    fun addResolver(executor: ConditionResolver) {
        conditionResolvers.add(executor)
    }

    fun resolve(conditionString: String, keys: Map<String, Key>): Boolean {
        if (conditionString.isBlank()) {
            return true
        }
        return resolveGroups(conditionString, keys)
    }

    private fun resolveGroups(conditionString: String, keys: Map<String, Key>): Boolean {
        var condition = conditionString.trim()
        var hasGroups = GROUP.containsMatchIn(condition)
        if (hasGroups) {
            while (hasGroups) {
                val matchResults = GROUP.findAll(condition)
                matchResults.forEach { matchResult ->
                    if (matchResult.groups.size == 2) {
                        val matchPart = matchResult.groups[0]
                        val conditionPart = matchResult.groups[1]
                        if (matchPart != null && conditionPart != null) {
                            val result = resolveGroup(conditionPart.value, keys)
                            condition = condition.replace(matchPart.value, result.toString())
                        }
                    }
                }
                hasGroups = GROUP.matches(condition)
            }
        }
        return resolveGroup(condition, keys)
    }

    private fun resolveGroup(conditionString: String, keys: Map<String, Key>): Boolean {
        var condition = conditionString.trim()
        if (hasAnyOperation(condition)) {
            var hasOperation = true
            while (hasOperation) {
                while (hasAnd(condition)) {
                    condition = performNextAndOperation(condition, keys)
                }
                while (hasOr(condition)) {
                    condition = performNextOrOperation(condition, keys)
                }
                hasOperation = hasAnyOperation(condition)
            }
        }
        return resolveGroupPart(condition, keys)
    }

    private fun hasAnd(conditionString: String): Boolean = conditionString.contains(AND)
    private fun hasOr(conditionString: String): Boolean = conditionString.contains(OR)
    private fun hasAnyOperation(conditionString: String): Boolean = hasAnd(conditionString) || hasOr(conditionString)

    private fun performNextAndOperation(conditionString: String, keys: Map<String, Key>): String {
        return performNextOperation(AND, conditionString, keys) { part1, part2 -> part1 && part2 }
    }

    private fun performNextOrOperation(conditionString: String, keys: Map<String, Key>): String {
        return performNextOperation(OR, conditionString, keys) { part1, part2 -> part1 || part2 }
    }

    private fun performNextOperation(
        operator: Char,
        conditionString: String,
        keys: Map<String, Key>,
        resolveOperation: (Boolean, Boolean) -> Boolean
    ): String {
        if (!conditionString.contains(operator))
            return conditionString
        var condition = conditionString.trim()
        val expressionToReplace: StringBuilder = StringBuilder()
        val part1: StringBuilder = StringBuilder()
        val part2: StringBuilder = StringBuilder()
        var currentPart = 1
        for (letter in condition) {
            if (isAnyOperator(letter) && currentPart == 2) {
                //Any operator detected when capture part2 => End of capture part2
                //Resolve operation
                break
            } else if (isAnyOperator(letter, except = operator) && currentPart == 1) {
                //Other operator detected when capture part1 => Wrong operation
                //Reset operation capture
                part1.clear()
                expressionToReplace.clear()
            } else if (isOperator(letter, operator) && currentPart == 1) {
                //Good operator detected when capture part1 => End of capture part1
                //Capture part2
                currentPart = 2
                expressionToReplace.append(letter)
            } else {
                //No operator detected => capture current part
                if (currentPart == 1) {
                    part1.append(letter)
                } else if (currentPart == 2) {
                    part2.append(letter)
                }
                expressionToReplace.append(letter)
            }
        }
        //End of condition
        if (currentPart == 2) {
            val result1 = resolveGroupPart(part1.toString(), keys)
            val result2 = resolveGroupPart(part2.toString(), keys)
            val result = resolveOperation(result1, result2)
            condition = condition.replace(expressionToReplace.toString(), result.toString())
            return condition
        }
        return condition
    }

    private fun isAnyOperator(char: Char) = char == AND || char == OR
    private fun isAnyOperator(char: Char, except: Char) = isAnyOperator(char) && except != except
    private fun isOperator(char: Char, wantedOperator: Char) = char == wantedOperator

    private fun resolveGroupPart(conditionString: String, keys: Map<String, Key>): Boolean {
        conditionResolvers.forEach { resolver ->
            val condition: Condition? = resolver.getCondition(conditionString)
            if (condition != null) {
                return condition.perform(keys[condition.keyName])
            }
        }
        return true //No condition found
    }

    companion object {
        private const val AND: Char = '&'
        private const val OR: Char = '|'
        private val GROUP = "\\((.[^\\(\\)]*)\\)".toRegex()


    }
}