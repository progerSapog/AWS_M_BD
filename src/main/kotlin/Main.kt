import model.dao.entity.CQCElementEntity
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main(args: Array<String>) {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/aws_m_cqc",
        driver = "org.postgresql.Driver",
        user = "administrator",
        password = "admin12"
    )

    println(database)
    println("Hello World!")

    val elements = database
        .from(CQCElementEntity)
        .select()
        .orderBy(CQCElementEntity.id.asc())
        .map { row -> CQCElementEntity.createEntity(row) }
    elements.forEach { println(it) }
    println(elements)
    println(elements[0].parentId)

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}