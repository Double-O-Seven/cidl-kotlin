package ch.leadrian.samp.kamp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.InterfaceDefinitionUnit

class DeclarationsVisitor(
        private val constantDeclarationVisitor: ConstantDeclarationVisitor,
        private val functionDeclarationVisitor: FunctionDeclarationVisitor
) : CIDLBaseVisitor<InterfaceDefinitionUnit>() {

    override fun visitDeclarations(ctx: CIDLParser.DeclarationsContext): InterfaceDefinitionUnit {
        val constants: List<Constant> = visitContantDeclarations(ctx)
        val functions: List<Function> = visitFunctionDeclarations(ctx)
        return InterfaceDefinitionUnit(
                constants = constants,
                functions = functions
        )
    }

    private fun visitFunctionDeclarations(ctx: CIDLParser.DeclarationsContext): List<Function> =
            ctx.declaration()
                    .asSequence()
                    .mapNotNull { it.functionDeclaration() }
                    .map { functionDeclarationVisitor.visit(it) }
                    .toList()

    private fun visitContantDeclarations(ctx: CIDLParser.DeclarationsContext): List<Constant> =
            ctx
                    .declaration()
                    .asSequence()
                    .mapNotNull { it.constantDeclaration() }
                    .map { constantDeclarationVisitor.visit(it) }
                    .toList()

}