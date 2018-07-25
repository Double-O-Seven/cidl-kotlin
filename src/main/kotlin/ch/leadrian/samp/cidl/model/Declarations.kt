package ch.leadrian.samp.cidl.model

data class Declarations(
        val constants: List<Constant> = emptyList(),
        val functions: List<Function> = emptyList()
)