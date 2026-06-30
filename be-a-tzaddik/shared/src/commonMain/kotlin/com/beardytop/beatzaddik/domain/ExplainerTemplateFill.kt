package com.beardytop.beatzaddik.domain

/** Fills catalog template placeholders (`{key}` or `$key`) without UI-layer dependencies. */
object ExplainerTemplateFill {
    fun fill(template: String, args: Map<String, String>): String {
        var out = template
        for ((key, value) in args) {
            out = out.replace("{$key}", value).replace("$" + key, value)
        }
        return out
    }
}
