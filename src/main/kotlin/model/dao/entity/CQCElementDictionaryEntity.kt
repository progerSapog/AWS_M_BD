package model.dao.entity

import model.dao.signature.CQCElementDictionary
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import configuration.CQCSchema.*

object CQCElementDictionaryEntity : Table<CQCElementDictionary>(
    schema = SCHEMA.value,
    tableName = CQC_ELEM_DICT.value
) {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}