import kotlin.random.Random

var count = 0 // Это счётчик для остановки исполнения кода через 30 циклов
val dayLimit = 150_000
val monthLimit = 600_000
val availableCards = listOf("Мир", "Mastercard", "Visa")
// Mastercard
val mastercardTaxStart = 75_000
val mastercardTaxPercent = 0.006
val mastercardTaxConstant = 20
var mastercardDayTransferTotal = 0 // На текущем этапе курса Kotlin пишу без геттеров и сеттеров, игнорируя безопасность
var mastercardMonthTransferTotal = 0
// Visa
val visaTaxPercent = 0.0075
val visaTaxMin = 35
var visaDayTransferTotal = 0
var visaMonthTransferTotal = 0
// Mir
var mirDayTransferTotal = 0
var mirMonthTransferTotal = 0

fun main() {
    while (count < 30) {
        count += 1
        // В каждой новой итерации с вероятностью 10% сбрасывается лимит на день
        if (Random.nextDouble() < 0.1) {
            mastercardDayTransferTotal = 0
            visaDayTransferTotal = 0
            mirDayTransferTotal = 0
            println("Настал новый день. Лимиты переводов на день сброшены.")
            println()
        }
        // Рандомно назначаю карту и сумму перевода в текущей итерации
        val cardType = availableCards.random()
        val transfer = (1500..50_000).random()

        println("Карта $cardType; сумма перевода $transfer руб.; лимит сут/мес на момент перевода " + when (cardType) {
                    "Mastercard" -> "$mastercardDayTransferTotal/$mastercardMonthTransferTotal руб."
                    "Visa" -> "$visaDayTransferTotal/$visaMonthTransferTotal руб."
                    else -> "$mirDayTransferTotal/$mirMonthTransferTotal руб."
                })
        countTax(transfer, cardType, when (cardType) {
            "Mastercard" -> mastercardMonthTransferTotal
            "Visa" -> visaMonthTransferTotal
            else -> mirMonthTransferTotal
        })
    }
}

fun countTax(transfer: Int, cardType: String = "Мир", currentTransferTotal: Int) {
    when (cardType) {
        "Mastercard" ->
            when {
                    transfer + currentTransferTotal > monthLimit -> {
                    println("Превышен лимит месячных переводов!")
                }
                    transfer + mastercardDayTransferTotal > dayLimit -> {
                    println("Превышен лимит дневных переводов!")
                }
                    transfer + mastercardDayTransferTotal <= mastercardTaxStart -> {
                        mastercardMonthTransferTotal += transfer
                        mastercardDayTransferTotal += transfer
                        println("Комиссия за перевод составила 0 руб. " +
                                "(начнёт взиматься при превышении общей суммы переводов в день - $mastercardTaxStart руб.)")
                }
                    else -> {
                        val taxCount: Int =
                            ((transfer + mastercardDayTransferTotal - mastercardTaxStart) * mastercardTaxPercent + mastercardTaxConstant).toInt()
                        mastercardMonthTransferTotal += transfer
                        mastercardDayTransferTotal += transfer
                        println("Комиссия за перевод составила $taxCount руб.")
                }
            }
        "Visa" ->
            when {
                transfer + currentTransferTotal > monthLimit -> {
                    println("Превышен лимит месячных переводов!")
                }
                transfer + visaDayTransferTotal > dayLimit -> {
                    println("Превышен лимит дневных переводов!")
                }
                else -> {
                    visaMonthTransferTotal += transfer
                    visaDayTransferTotal += transfer
                    println("Комиссия за перевод составила ${(transfer * visaTaxPercent + visaTaxMin).toInt()} руб.")
                }
            }
        "Мир" ->
            when {
                transfer + currentTransferTotal > monthLimit -> {
                    println("Превышен лимит месячных переводов!")
                }
                transfer + mirDayTransferTotal > dayLimit -> {
                    println("Превышен лимит дневных переводов!")
                }
                else -> {
                    mirMonthTransferTotal += transfer
                    mirDayTransferTotal += transfer
                    println("Комиссия за перевод с карты Мир не предусмотрена, составила 0 руб.")
                }
            }
        }
    }