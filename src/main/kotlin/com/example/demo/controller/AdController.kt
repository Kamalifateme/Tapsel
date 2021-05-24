package com.example.demo.controller

import com.example.demo.model.AdEvent
import com.example.demo.repository.AdRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:8081"])
@RestController
@RequestMapping("/api")
class AdController {
    @Autowired
    private lateinit var adRepository: AdRepository

    @GetMapping("/ads/{requestId}")
    fun getAdById(@PathVariable("requestId") requestId: String): ResponseEntity<AdEvent> {
        val adData: AdEvent? = adRepository.findByRequestId(requestId)

        return if (adData != null) {
            ResponseEntity<AdEvent>(adData, HttpStatus.OK)
        } else {
            ResponseEntity<AdEvent>(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/ads")
    fun createAd(@RequestBody ad: AdEvent): ResponseEntity<AdEvent> {
        return try {
            val adEvent: AdEvent = adRepository.save(
                AdEvent(
                    ad.requestId,
                    ad.adId,
                    ad.adTitle,
                    ad.advertiserCost,
                    ad.appId,
                    ad.appTitle,
                    ad.impressionTime,
                    ad.clickTime
                )
            )
            ResponseEntity<AdEvent>(adEvent, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity<AdEvent>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/ads/{requestId}")
    fun updateAd(@PathVariable("requestId") requestId: String, @RequestBody ad: AdEvent): ResponseEntity<AdEvent> {
        val adData: AdEvent? = adRepository.findByRequestId(requestId)

        return if (adData != null) {
            val adEvent: AdEvent = adData
            adEvent.requestId = ad.requestId
            adEvent.adId = ad.adId
            adEvent.adTitle = ad.adTitle
            adEvent.advertiserCost = ad.advertiserCost
            adEvent.appId = ad.appId
            adEvent.appTitle = ad.appTitle
            adEvent.impressionTime = ad.impressionTime
            adEvent.clickTime = ad.clickTime
            ResponseEntity<AdEvent>(adRepository.save(adEvent), HttpStatus.OK)
        } else {
            ResponseEntity<AdEvent>(HttpStatus.NOT_FOUND)
        }
    }
}