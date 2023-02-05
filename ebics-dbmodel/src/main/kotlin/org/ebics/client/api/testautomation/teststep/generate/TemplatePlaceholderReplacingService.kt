package org.ebics.client.api.testautomation.teststep.generate

import org.ebics.client.api.bankconnection.properties.BankConnectionPropertyEntity
import org.ebics.client.api.uploadtemplates.FileTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class TemplatePlaceholderReplacingService {
    fun replaceTemplatePlaceholdersWithValues(
        userId: String,
        template: FileTemplate,
        properties: Collection<BankConnectionPropertyEntity>
    ): ByteArray {
        //The replacement is done in two steps (preprocessing and postprocessing)
        //Because there are some variables which are first calculated by preprocessing (like nbOfTrxs & ctrlSum)
        //And first then properly replaced by postprocessing
        val preprocessingRegExpParts = arrayOf(
            "%%aLevelId%%", "%%bLevelId(-keep)?%%", "%%cLevelId(-keep)?%%", "%%UETR(-keep)?%%",
            "%%IsoDtTm(([+-])(\\d+))?%%", "%%IsoDt(([+-])(\\d+))?%%",
            "<InstdAmt Ccy=\"\\w{3}\">(.*)</InstdAmt>", "<Amt Ccy=\"\\w{3}\">(.*)</Amt>"
        )
        val preprocessingRegExp = Regex.fromLiteral(preprocessingRegExpParts.joinToString("|") { regExpOrPart -> "($regExpOrPart)" })

        val idPrefix = userId + "-" + uniqueTimeStamp()
        var bLevel = 0;
        var cLevel = 0;
        var uetr = UUID.randomUUID().toString()
        var nbOfTrxs = 0;
        var ctrlSum: BigDecimal = BigDecimal.ZERO

        val preprocessingResult = preprocessingRegExp.replace(template.fileContentText) { match ->
            when {
                match.value.startsWith("%%aLevelId%%") -> idPrefix
                match.value.startsWith("%%bLevelId") -> {
                    if (!match.value.contains("-keep")) {
                        cLevel = 0
                        bLevel++
                    }
                    "${idPrefix}-B${bLevel}";
                }
                match.value.startsWith("%%cLevelId") -> {
                    if (!match.value.contains("-keep")) {
                        cLevel++
                        nbOfTrxs++
                    }
                    "${idPrefix}-B${bLevel}-C${cLevel}"
                }
                match.value.startsWith("%%IsoDtTm") -> {
                    val actualDateTime = LocalDateTime.now()
                    val adjustedDateTime = if (match.groupValues.size == 4) {
                        val dayOffset = match.groupValues[3].toLong()
                        if (match.groupValues[2] == "+")
                            actualDateTime.plusDays(dayOffset)
                        else
                            actualDateTime.minusDays(dayOffset)
                    } else
                        actualDateTime
                    adjustedDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
                }
                match.value.startsWith("%%IsoDt") -> {
                    val actualDateTime = LocalDate.now()
                    val adjustedDateTime = if (match.groupValues.size == 4) {
                        val dayOffset = match.groupValues[3].toLong()
                        if (match.groupValues[2] == "+")
                            actualDateTime.plusDays(dayOffset)
                        else
                            actualDateTime.minusDays(dayOffset)
                    } else
                        actualDateTime
                    adjustedDateTime.format(DateTimeFormatter.ISO_DATE)
                }
                match.value.startsWith("%%UETR") -> {
                    if (!match.value.contains("-keep"))
                        uetr = UUID.randomUUID().toString()
                    uetr
                }
                match.value.startsWith("<InstdAmt") || match.value.startsWith("<Amt") -> {
                    val amount = match.groupValues[1].toBigDecimal()
                    ctrlSum += amount
                    match.value
                }
                else -> "unknown placeholder: ${match.value}"
            }
        }

        val postprocessingRegExpParts = arrayOf("%%NbOfTxs-aLevel%%", "%%CtrlSum%%", "%%(.{3,30}?)%%")
        val postprocessingRegExp = Regex.fromLiteral(postprocessingRegExpParts.joinToString("|") { regExpOrPart -> "($regExpOrPart)" })

        val postprocessingResult = postprocessingRegExp.replace(preprocessingResult) { match ->
            when {
                match.value == "%%NbOfTxs-aLevel%%" -> nbOfTrxs.toString()
                match.value == "%%CtrlSum%%" ->  ctrlSum.toString()
                match.value.startsWith("%%") && match.value.endsWith("%%") -> {
                    val propertyKeyName = match.groupValues[1]
                    val propertyValue = properties.find { property -> property.key == propertyKeyName }?.value
                    propertyValue ?: "property value not defined for $propertyKeyName"
                }
                else -> "unknown placeholder: ${match.value}"
            }
        }

        return postprocessingResult.toByteArray(Charsets.UTF_8)
    }


    /**
     *
     * @returns unique timestamp within 1 year in MMdd-hhmmss format
     */
    private fun uniqueTimeStamp(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd-hhmmss"))
    }
}

val integerChars = '0'..'9'
fun String.isInteger(): Boolean = this.all { it in integerChars }