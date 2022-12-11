package configuration

import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

object LiquibaseMigrator {
    private const val NO_CONTEXT_MODE = ""

    fun migrate(database: DataSource, changeFile: String) {
        val connection = database.connection
        connection.use {
            val migrator = Liquibase(changeFile, ClassLoaderResourceAccessor(), JdbcConnection(connection))
            migrator.update(Contexts())
        }
    }
}