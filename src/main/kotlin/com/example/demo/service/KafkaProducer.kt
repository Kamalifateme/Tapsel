package com.example.demo.service

import com.example.demo.model.ClickEvent
import com.example.demo.model.ImpressionEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.javafaker.Faker
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class KafkaProducer {
    @Autowired
    private lateinit var producer: KafkaTemplate<String, String>

    fun dataGenerator() {
        while (true) {
            val faker = Faker()
            val requstId = faker.random().toString()
            val fakeImpression = ImpressionEvent(
                requestId = requstId,
                adId = faker.random().toString(),
                adTitle = faker.name().title(),
                advertiserCost = faker.commerce().price(0.0, 10000.0).toDouble(),
                appId = faker.random().toString(),
                appTitle = faker.name().title(),
                impressionTime = System.currentTimeMillis()
            )
            val fakeClick = ClickEvent(
                requestId = requstId,
                clickTime = System.currentTimeMillis()
            )
            val jsonMapper = ObjectMapper().apply {
                registerKotlinModule()
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                dateFormat = StdDateFormat()
            }
            val fakeImpressionJson = jsonMapper.writeValueAsString(fakeImpression)
            val fakeClickJson = jsonMapper.writeValueAsString(fakeClick)
            var futureResult = producer.send(ProducerRecord("Impressions", fakeImpressionJson))
            futureResult.get()
            futureResult = producer.send(ProducerRecord("Clicks", fakeClickJson))
            futureResult.get()
        }
    }
}