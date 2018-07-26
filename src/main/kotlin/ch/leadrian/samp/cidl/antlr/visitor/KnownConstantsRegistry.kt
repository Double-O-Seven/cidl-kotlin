package ch.leadrian.samp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.model.Constant

class KnownConstantsRegistry {

    private val knownConstantsByName: MutableMap<String, Constant> = mutableMapOf()

    fun register(constant: Constant) {
        if (isKnown(constant.name)) {
            throw IllegalArgumentException("Constant with name '${constant.name}' already exists")
        }
        knownConstantsByName[constant.name] = constant
    }

    fun getConstant(name: String): Constant? = knownConstantsByName[name]

    operator fun get(name: String) = getConstant(name)

    fun isKnown(name: String): Boolean = knownConstantsByName.containsKey(name)
}