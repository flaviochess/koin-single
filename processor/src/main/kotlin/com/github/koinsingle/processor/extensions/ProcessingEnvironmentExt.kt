package com.github.koinsingle.processor.extensions

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

fun ProcessingEnvironment.error(e: Element, msg: String) {
    this.messager.printMessage(Diagnostic.Kind.ERROR, msg, e)
}