package com.github.koinsingle.processor

import com.github.koinsingle.annotation.Single
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class SingleProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedSourceVersion(): SourceVersion =
            SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
            mutableSetOf(Single::class.qualifiedName.toString())

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {

        val elements = mutableListOf<Element>()

        roundEnv.getElementsAnnotatedWith(Single::class.java).forEach {

            if (it.kind != ElementKind.CLASS) {

                error(it, "Only classes can be annotated with @${Single::class.simpleName}")
                return false
            }

            elements.add(it)
        }

        generateModule(elements)

        return true
    }

    private fun generateModule(elements: List<Element>) {

        if (elements.isEmpty()) return

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val generatedClassName = "AutoGeneratedModule"
        val generatedPackageName = "com.github.koinsingle.modules"

        val module = ClassName("org.koin.dsl", "module")

        val generatedModules = FunSpec.builder("generatedModules")
                .beginControlFlow("return %T", module)

        elements.forEach {
            generatedModules.addStatement("single { %T() } ", it)
        }

        generatedModules
                .endControlFlow()

        val file = FileSpec.builder(generatedPackageName, generatedClassName)
                .addType(
                        TypeSpec.classBuilder(generatedClassName)
                                .addFunction(
                                        generatedModules.build())
                                .build()
                )
                .build()

        file.writeTo(File(kaptKotlinGeneratedDir))
    }

    private fun error(e: Element?, msg: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg, e)
    }
}