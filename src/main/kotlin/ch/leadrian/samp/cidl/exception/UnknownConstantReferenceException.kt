package ch.leadrian.samp.cidl.exception

class UnknownConstantReferenceException(
        constantName: String,
        expression: String
) : RuntimeException("Unknown constant reference '$constantName' in expression <$expression>")