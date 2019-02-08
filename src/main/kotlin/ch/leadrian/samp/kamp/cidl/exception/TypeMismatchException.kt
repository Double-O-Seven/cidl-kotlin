package ch.leadrian.samp.kamp.cidl.exception

class TypeMismatchException(
        expectedType: String,
        foundType: String,
        expression: String
) : RuntimeException("Expected type '$expectedType', but found type '$foundType' in expression <$expression>")
