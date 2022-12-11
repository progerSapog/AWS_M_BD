package model.dao.entity

import model.dao.signature.CQCElement
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import configuration.CQCSchema.*

object CQCElementEntity : Table<CQCElement>(
    schema = SCHEMA.value,
    tableName = CQC_ELEM.value
) {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val parent = uuid("parent_id").bindTo { it.parentId }
    val type = uuid("type").references(CQCElementDictionaryEntity) { it.type }
    val value = varchar("value").bindTo { it.value }
}