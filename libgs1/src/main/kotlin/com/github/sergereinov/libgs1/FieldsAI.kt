package com.github.sergereinov.libgs1

class FieldsAI private constructor(
    val list: List<FieldAI>,
    val hasError: Boolean
) {

    companion object {

        fun from(gs1Stream: CharSequence): FieldsAI {

            val list = mutableListOf<FieldAI>()
            var chars = gs1Stream
            var referencePos: Int = 0

            while(chars.isNotEmpty()) {
                val f = parseField(chars, referencePos) ?: break
                list.add(f)
                referencePos += f.referenceLen
                chars = chars.drop(f.referenceLen)
            }

            val hasError = chars.isNotEmpty()

            return FieldsAI(list, hasError)
        }

        private fun parseField(field: CharSequence, referencePos: Int): FieldAI? {

            val ai = AI.from(field) ?: return null
            val aiLen = ai.ai.length
            val body = copyGs1Body(ai, field.drop(aiLen)) ?: return null

            return FieldAI(
                ai = ai,
                textAi = field.take(aiLen).toString(),
                textBody = body.text,
                referencePos = referencePos,
                referenceLen = aiLen + body.len
            )
        }

        private data class FieldAIBody(val text: String, val len: Int)

        private fun copyGs1Body(ai: AI, body: CharSequence): FieldAIBody? {

            val minLen = ai.fieldLen - ai.ai.length
            if (body.length < minLen) return null

            val maxLen =
                body.length.coerceAtMost(
                    if (ai.maxFieldLenOptional > 0) ai.maxFieldLenOptional - ai.ai.length else minLen
                )

            var len = 0
            while (len < maxLen && body[len] != AI.CONTROL_GS) len++
            val textLen = len
            while (len < maxLen && body[len] == AI.CONTROL_GS) len++

            return if (len < minLen) null else FieldAIBody(body.take(textLen).toString(), len)
        }
    }
}
