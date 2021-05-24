package com.example.demo.service

import com.example.demo.model.AdEvent
import com.example.demo.model.ClickEvent
import com.example.demo.model.ImpressionEvent
import com.example.demo.repository.AdRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service("AdService")
class AdService {
    @Autowired
    private lateinit var adRepository: AdRepository


    private lateinit var jsonMapper: ObjectMapper

    @PostConstruct
    fun init(){
        jsonMapper = ObjectMapper().apply {
            registerKotlinModule()
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            dateFormat = StdDateFormat()
        }
    }

    @KafkaListener(topics = ["Impressions"])
    fun impressionRecorder(@Payload clickJson: String) {
        val adImpression = jsonMapper.readValue(clickJson, ImpressionEvent::class.java)
        val adEvent = AdEvent(
            requestId = adImpression.requestId,
            adId = adImpression.adId,
            adTitle = adImpression.adTitle,
            advertiserCost = adImpression.advertiserCost,
            appId = adImpression.appId,
            appTitle = adImpression.appTitle,
            impressionTime = adImpression.impressionTime,
            clickTime = 0
        )
        insertAd(adEvent)
    }

    @KafkaListener(topics = ["Clicks"])
    fun clickRecorder(@Payload clickJson: String) {
        val clickEvent = jsonMapper.readValue(clickJson, ClickEvent::class.java)
        updateAd(clickEvent.requestId, clickEvent.clickTime)
    }

    fun insertAd(adEvent: AdEvent) {
        adRepository.save(adEvent)
    }

    fun updateAd(requestId: String, clickTime: Long) {
        var adEvent = adRepository.findByRequestId(requestId)
        adEvent!!.clickTime = clickTime
        adRepository.save(adEvent)
    }
}