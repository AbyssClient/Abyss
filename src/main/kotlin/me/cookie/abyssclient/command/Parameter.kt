package me.cookie.abyssclient.command


import net.minecraft.text.TranslatableText

class ParameterValidationResult<T> private constructor(
    val errorMessage: String?,
    val mappedResult: T?
) {

    companion object {
        fun <T> ok(value: T): ParameterValidationResult<T> = ParameterValidationResult(null, value)
        fun <T> error(errorMessage: String): ParameterValidationResult<T> =
            ParameterValidationResult(errorMessage, null)
    }

}

typealias ParameterVerifier<T> = (String) -> ParameterValidationResult<T>
typealias AutoCompletionHandler = (String) -> List<String>

class Parameter<T>(
    val name: String,
    val required: Boolean,
    val vararg: Boolean,
    val verifier: ParameterVerifier<T>?,
    val autocompletionHandler: AutoCompletionHandler?,
    val useMinecraftAutoCompletion: Boolean,
    var command: Command? = null
) {
    val translationBaseKey: String
        get() = "${command?.translationBaseKey}.parameter.$name"

    val description: TranslatableText
        get() = TranslatableText("$translationBaseKey.description")
}