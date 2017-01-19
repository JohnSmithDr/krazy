package krazy.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Attr(val selector: String, val attr: String)