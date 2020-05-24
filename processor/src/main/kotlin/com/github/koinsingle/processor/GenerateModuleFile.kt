package com.github.koinsingle.processor

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.processor.extensions.error
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class GenerateModuleFile(
        private val processingEnv: ProcessingEnvironment
) {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        const val PACKAGE_NAME = "com.github.koinsingle.modules"
        const val FILE_NAME = "AutoGeneratedModule"
        const val OBJECT_NAME = "AutoGeneratedModule"
    }

    fun generate(annotatedClassElements: List<TypeElement>) {

        if (annotatedClassElements.isEmpty()) return

        val moduleCodeBlock = generateModuleCodeBlock(annotatedClassElements)
        val moduleProperty = generateModulesProperty(moduleCodeBlock)
        val autoGeneratedModuleObject = generateObject(moduleProperty)
        val autoGeneratedModuleFile = generateFile(autoGeneratedModuleObject)

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        autoGeneratedModuleFile.writeTo(File(kaptKotlinGeneratedDir))
    }

    private fun generateModuleCodeBlock(annotatedClassElements: List<TypeElement>) : CodeBlock {

        val moduleDsl = ClassName("org.koin.dsl", "module")

        val generatedModules = CodeBlock.builder()
                .beginControlFlow("%T", moduleDsl)

        annotatedClassElements.sortedBy {
            it.simpleName.toString()
        }.forEach {
            try {
                addSingle(generatedModules, it)
            } catch (ex: ProcessingException) {
                processingEnv.error(ex.element, ex.message)
            }
        }

        generatedModules
                .endControlFlow()

        return generatedModules.build()
    }

    private fun addSingle(generatedModulesBuilder: CodeBlock.Builder, annotatedClassElement: TypeElement) {

        val constructorElement = annotatedClassElement.enclosedElements.firstOrNull {
            it.kind == ElementKind.CONSTRUCTOR &&
                    it.modifiers.contains(Modifier.PUBLIC)
        } ?: throw ProcessingException(
                annotatedClassElement,
                "The class ${annotatedClassElement.simpleName} annotated with ${Single::class.simpleName} " +
                        "must have a public constructor"
        )

        val constructor = constructorElement as ExecutableElement

        val constructorParameters = constructor.parameters.joinToString(separator = ",") {
            " ".plus(generateConstructorParameter(it))
        }

        annotatedClassElement.interfaces.forEach {
            val singleWithInterface = "single<%T> { %T($constructorParameters) }"
            generatedModulesBuilder.addStatement(singleWithInterface, it, annotatedClassElement)
        }

        val single = "single { %T($constructorParameters) }"
        generatedModulesBuilder.addStatement(single, annotatedClassElement)
    }

    private fun generateConstructorParameter(parameter: VariableElement): String = "get()"

    private fun generateModulesProperty(moduleCodeBlock: CodeBlock): PropertySpec {

        val moduleClass = ClassName("org.koin.core.module", "Module")

        return PropertySpec.builder("modules", moduleClass)
                .initializer(moduleCodeBlock)
                .build()
    }

    private fun generateObject(moduleProperty: PropertySpec): TypeSpec {

        return TypeSpec.objectBuilder(OBJECT_NAME)
                .addProperty(moduleProperty)
                .build()
    }

    private fun generateFile(objectType: TypeSpec): FileSpec {

        return FileSpec.builder(PACKAGE_NAME, FILE_NAME)
                .addType(objectType)
                .build()
    }
}
