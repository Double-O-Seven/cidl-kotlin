package ch.leadrian.samp.cidl.parser

import java.io.InputStream

interface InterfaceDefinitionSource {

    fun getInputStream(): InputStream

}