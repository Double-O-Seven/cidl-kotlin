package ch.leadrian.samp.kamp.cidl.model

data class InterfaceDefinitionUnit(
        val constants: List<Constant> = emptyList(),
        val functions: List<Function> = emptyList()
)