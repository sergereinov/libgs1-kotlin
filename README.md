## GS1 barcode (EAN/UPC, Databar barcodes) parser library

This library parses the GS1 fields stream according to the [GS1 specification](https://www.gs1.org/docs/barcodes/GS1_General_Specifications.pdf).

### Usage

Getting the GS1 fields from a stream
```kotlin
val fields = FieldsAI.from(gs1Stream)
```
`FieldsAI.from` will return an object with attributes
```kotlin
class FieldsAI(
    val list: List<FieldAI>,
    val hasError: Boolean
)
```

Each field in the resulting list of fields `List<FieldAI>` will contain the following information
```kotlin
data class FieldAI(
    val ai: AI,
    val textAi: String,
    val textBody: String,
    val referencePos: Int,
    val referenceLen: Int
)
```
Where `ai` is the complete description of this GS1 AI tag,
`textAi` is the actual AI tag code from the stream,
`textBody` is the body content of this GS1 field,
`referencePos` is the starting position of this GS1 field in the source stream,
`referenceLen` is the length of the GS1 field in the source stream.

Example
```kotlin
val gs1Stream = "010061414199999610ABCDEF123456\u001D21654321FEDCBA\u001D310200123411140823"
val fields = FieldsAI.from(gs1Stream)

val gtin = fields.list.firstOrNull { it.ai.ai == "01" }
val gtinText: String? = gtin?.textBody
println("AI 01: product identity code = \"$gtinText\"")

val net = fields.list.firstOrNull { it.ai.ai == "310n" }
val netWeight: Float? = net?.formatBody()?.toFloatOrNull()
println("AI 310n: net weight = $netWeight kg")

val some = fields.list.firstOrNull {
    with(it.ai.dataTitle) {
        contains("net", true) && contains("weight", true)
    }
}
println("Some net weight: AI group = ${some?.ai?.ai}, AI code = ${some?.textAi}, weight = ${some?.formatBody()} kg")
```
Prints
```
AI 01: product identity code = "00614141999996"
AI 310n: net weight = 12.34 kg
Some net weight: AI group = 310n, AI code = 3102, weight = 12.34 kg
```

You can find more examples of usage in the unit tests inside the project.

## Installation

Download the latest release files. Place .jar file(s) in the `<project>/app/libs` folder (in case of Android project).
Add dependensies to the `build.gradle` script of your app module.
```gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    ...
}
```
Run gradle sync. Done.
