package com.nahlasamir244.currencyapp.annotation

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds.
 */
@OpenClass
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class OpenForTesting
