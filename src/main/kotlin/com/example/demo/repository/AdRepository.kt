package com.example.demo.repository

import com.example.demo.model.AdEvent
import org.springframework.data.cassandra.repository.AllowFiltering
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AdRepository : CassandraRepository<AdEvent?, UUID?> {
    @AllowFiltering
    fun findByRequestId(requestId: String): AdEvent?
}