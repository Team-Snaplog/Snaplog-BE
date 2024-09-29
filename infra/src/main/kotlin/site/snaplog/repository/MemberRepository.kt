package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import site.snaplog.entity.MemberEntity

interface MemberRepository: R2dbcRepository<MemberEntity, Long>