package com.example.gasolinaetanol.data

enum class FuelRecommendation {
    ETHANOL, GASOLINE, EQUAL, INVALID
}

data class VehicleConsumption(
    val ethanolKml: Double = 7.0,
    val gasolineKml: Double = 10.0
) {
    val efficiencyRatio: Double
        get() = if (gasolineKml > 0) ethanolKml / gasolineKml else 0.70
}

data class FuelPrices(
    val ethanolPrice: Double,
    val gasolinePrice: Double,
    val customEfficiencyRatio: Double = 0.70
)

class FuelCalculatorRepository {
    fun calculateBestOption(prices: FuelPrices): FuelRecommendation {
        if (prices.gasolinePrice <= 0 || prices.ethanolPrice <= 0) return FuelRecommendation.INVALID
        
        val ratio = prices.ethanolPrice / prices.gasolinePrice
        return when {
            ratio < prices.customEfficiencyRatio -> FuelRecommendation.ETHANOL
            ratio > prices.customEfficiencyRatio -> FuelRecommendation.GASOLINE
            else -> FuelRecommendation.EQUAL
        }
    }

    fun calculateCostPerKm(price: Double, kml: Double): Double {
        if (kml <= 0) return 0.0
        return price / kml
    }
}
