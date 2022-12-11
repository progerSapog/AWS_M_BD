package model.dao.signature

import org.ktorm.entity.Entity
import java.util.UUID

interface CQCElement : Entity<CQCElement> {
    companion object : Entity.Factory<CQCElement>()

    val id: UUID
    var parentId: UUID?
    var type: CQCElementDictionary
    var value: String
}