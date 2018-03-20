package de.codefrog.demokotlin

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import java.time.LocalDate
import java.util.stream.LongStream
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class DemoKotlinApplication(val todoRepository: TodoRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        LongStream
                .range(1, 1000)
                .mapToObj { i -> Todo(0, "Todo $i", LocalDate.now().plusDays(i)) }
                .forEach { t -> todoRepository.save(t) }
        todoRepository.findAll().stream().forEach(System.out::println)
    }
}

fun main(args: Array<String>) {
    runApplication<DemoKotlinApplication>(*args)
}

@RepositoryRestResource
interface TodoRepository : JpaRepository<Todo, Long> {
    @RestResource(path = "by-description")
    fun findByDescription(@Param("description") description: String): Collection<Todo>
}

@Entity
data class Todo(
        @Id @GeneratedValue
        val id: Long = 0,
        val description: String? = null,
        val due: LocalDate? = null
)