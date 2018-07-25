package ch.leadrian.samp.cidl.model

data class Function(
        val type: String,
        val name: String,
        val parameters: List<Parameter> = emptyList(),
        val attributes: List<Attribute> = emptyList()
) {

    fun hasAttribute(name: String): Boolean =
            attributes.asSequence().map { it.name }.contains(name)

    fun getAttribute(name: String, defaultValue: Value? = null): Attribute =
            attributes.firstOrNull { it.name == name } ?: Attribute(name, defaultValue)
}