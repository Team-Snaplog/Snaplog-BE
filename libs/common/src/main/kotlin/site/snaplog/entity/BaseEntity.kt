package site.snaplog.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
abstract class BaseEntity {
    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime? = null
        private set

    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: LocalDateTime? = null
        private set
}