package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import site.snaplog.entity.SubjectEntity

interface SubjectRepository: R2dbcRepository<SubjectEntity, Long>