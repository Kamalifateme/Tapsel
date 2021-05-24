package com.example.demo.model

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table
class AdEvent {
    @PrimaryKey var requestId: String
    var adId: String
    var adTitle: String
    var advertiserCost: Double
    var appId: String
    var appTitle: String
    var impressionTime: Long
    var clickTime: Long

    constructor(
        requestId: String,
        adId: String,
        adTitle: String,
        advertiserCost: Double,
        appId: String,
        appTitle: String,
        impressionTime: Long,
        clickTime: Long
    ) {
        this.requestId = requestId
        this.adId = adId
        this.adTitle = adTitle
        this.advertiserCost = advertiserCost
        this.appId = appId
        this.appTitle = appTitle
        this.impressionTime = impressionTime
        this.clickTime = clickTime
    }


}