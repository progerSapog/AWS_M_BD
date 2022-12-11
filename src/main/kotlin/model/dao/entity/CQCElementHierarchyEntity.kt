package model.dao.entity

import model.dao.signature.CQCElementHierarchy
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import configuration.CQCSchema.*

object CQCElementHierarchyEntity : Table<CQCElementHierarchy>(
    schema = SCHEMA.value,
    tableName = CQC_ELEM_HIERARCHY.value
) {
    val childType = uuid("child_type").primaryKey().references(CQCElementDictionaryEntity) { it.childType }
    val parenType = uuid("parent_type").primaryKey().references(CQCElementDictionaryEntity) { it.parentType }
}