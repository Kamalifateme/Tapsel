package com.example.demo.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories


@Configuration
@ComponentScan("com")
@EnableCassandraRepositories("com.example.demo.repository")
class AppConfig {

}