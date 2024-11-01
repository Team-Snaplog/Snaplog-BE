package site.snaplog.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

open class BaseEntity(
    @CreatedDate
    @Column("created_at")
    open val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column("updated_at")
    open val updatedAt: LocalDateTime? = null
)