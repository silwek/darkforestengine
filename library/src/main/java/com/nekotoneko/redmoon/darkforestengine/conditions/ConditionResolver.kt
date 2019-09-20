package com.nekotoneko.redmoon.darkforestengine.conditions

/**
 * @author Silwèk on 2019-08-12
 */
interface ConditionResolver {
    fun getCondition(conditionString: String): Condition?
}

class DefaultConditionResolver : ConditionResolver {

    override fun getCondition(conditionString: String): Condition? {
        return extract(conditionString)
    }

    private fun extract(conditionString: String): Condition? {
        if (conditionString == "true" || conditionString == "false") {
            return BooleanCondition(conditionString.toBoolean())
        }

        var pair: Pair<String, String>?

        pair = extract(
            conditionString,
            LowerThanCondition.REGEX
        )
        if (pair != null)
            return LowerThanCondition(pair.first, pair.second)

        pair = extract(
            conditionString,
            GreaterThanCondition.REGEX
        )
        if (pair != null)
            return GreaterThanCondition(pair.first, pair.second)

        pair = extract(
            conditionString,
            EqualsToCondition.REGEX
        )
        if (pair != null)
            return EqualsToCondition(pair.first, pair.second)

        return null
    }

    private fun extract(conditionString: String, regex: Regex): Pair<String, String>? {
        val condition = conditionString.trim()
        val matches = regex.matches(condition)
        if (matches) {
            val matchResult = regex.find(condition)
            if (matchResult?.groups?.size == 3) {
                val key: String? = matchResult.groups[1]?.value
                val value: String? = matchResult.groups[2]?.value
                if (key != null && value != null)
                    return Pair(key, value)
            }
        }
        return null
    }
}