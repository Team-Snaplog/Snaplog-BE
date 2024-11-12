package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import site.snaplog.entity.TopicEntity

interface TopicRepository: R2dbcRepository<TopicEntity, Long>