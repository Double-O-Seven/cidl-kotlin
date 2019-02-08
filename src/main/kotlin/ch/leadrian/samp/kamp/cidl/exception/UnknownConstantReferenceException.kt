package ch.leadrian.samp.kamp.cidl.exception

class UnknownConstantReferenceException(
        constantName: String,
        expression: String
) : RuntimeException("Unknown constant reference '$constantName' in expression <$expression>")