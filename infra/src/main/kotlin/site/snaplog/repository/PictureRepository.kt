package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import site.snaplog.entity.PictureEntity

interface PictureRepository: R2dbcRepository<PictureEntity, Long>