package model.dao.entity

import com.codahale.metrics.MetricRegistry
import configuration.LiquibaseMigrator
import io.dropwizard.db.DataSourceFactory
import org.junit.jupiter.api.BeforeAll
import model.dao.entity.CQCElementEntity
import java.util.LinkedList
import java.util.UUID

object CQCElementDictionaryEntityTest {
    @BeforeAll
    fun setup() {
        val datasource = DataSourceFactory().apply {
            driverClass = "org.h2.Driver"
            user = ""
            password = ""
            url = "jdbc:h2:mem:test"
            charset("UTF-8")
        }.build(MetricRegistry(), "test")

        LiquibaseMigrator.migrate(datasource)
    }

    val competence = CQCElementEntity(UUID.randomUUID(), null, )

    fun `get all cqq elements without parameters`() {
        val cqcElements: List<CQCElementEntity> = LinkedList()

    }
}