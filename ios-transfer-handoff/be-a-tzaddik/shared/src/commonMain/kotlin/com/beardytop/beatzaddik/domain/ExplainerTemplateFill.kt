package com.beardytop.beatzaddik.domain

/** Fills catalog template placeholders (`{key}` or `$key`) without UI-layer dependencies. */
object ExplainerTemplateFill {
    fun fill(template: String, args: Map<String, String>, passes: Int = 2): String {
        if (args.isEmpty()) return template
        var out = template
        val keysByLength = args.keys.sortedByDescending { it.length }
        repeat(passes) {
            for (key in keysByLength) {
                val value = args[key] ?: continue
                out = out.replace("{$key}", value).replace("$" + key, value)
            }
        }
        return out
    }
}
