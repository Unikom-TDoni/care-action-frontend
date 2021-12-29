package edu.rpl.careaction.module.validation

abstract class FormValidation<TElement, out TResult: FormValidationResult<*>>(protected val element: TElement) {
    abstract fun validated() : TResult
}