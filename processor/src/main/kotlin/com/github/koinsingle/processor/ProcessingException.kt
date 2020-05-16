package com.github.koinsingle.processor

import java.lang.Exception
import javax.lang.model.element.Element

class ProcessingException(
        val element: Element,
        override val message: String
) : Exception(message)