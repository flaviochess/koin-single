package com.github.koinsingle.processor

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.processor.extensions.error
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class SingleProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion =
            SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
            mutableSetOf(Single::class.qualifiedName.toString())

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {

        val elements = mutableListOf<Element>()

        roundEnv.getElementsAnnotatedWith(Single::class.java).forEach {

            if (it.kind != ElementKind.CLASS) {

                processingEnv.error(it, "Only classes can be annotated with @${Single::class.simpleName}")
                return false
            }

            elements.add(it)
        }

        GenerateModuleFile(processingEnv).generate(elements)

        return true
    }

}
