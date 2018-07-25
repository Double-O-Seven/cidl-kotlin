package ch.leadrian.samp.cidl.exception

class DuplicateConstantDeclarationException(
        duplicateConstantName: String,
        expression: String
) : RuntimeException("Duplicate declaration of constant '$duplicateConstantName' in expression <$expression>")