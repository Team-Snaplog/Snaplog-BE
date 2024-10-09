package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import site.snaplog.entity.SnapEntity

interface SnapRepository: R2dbcRepository<SnapEntity, Long>