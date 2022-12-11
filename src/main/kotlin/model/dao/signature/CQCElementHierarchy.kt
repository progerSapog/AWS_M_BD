package model.dao.signature

import org.ktorm.entity.Entity

interface CQCElementHierarchy : Entity<CQCElementHierarchy> {
    companion object : Entity.Factory<CQCElementHierarchy>()

    val childType: CQCElementDictionary
    val parentType: CQCElementDictionary
}