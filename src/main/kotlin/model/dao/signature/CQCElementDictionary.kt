package model.dao.signature

import org.ktorm.entity.Entity
import java.util.UUID

interface CQCElementDictionary : Entity<CQCElementDictionary> {
    companion object : Entity.Factory<CQCElementDictionary>()

    val id: UUID
    var name: String
}