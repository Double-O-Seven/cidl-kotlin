package ch.leadrian.samp.kamp.cidl.model

data class Parameter(
        val type: String,
        val name: String,
        val defaultValue: Value? = null,
        val attributes: List<Attribute> = emptyList()
) {

    fun hasAttribute(name: String): Boolean =
            attributes.asSequence().map { it.name }.contains(name)

    fun getAttribute(name: String, defaultValue: Value? = null): Attribute =
            attributes.firstOrNull { it.name == name } ?: Attribute(name, defaultValue)
}