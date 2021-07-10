package br.sp.lhperez.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
			.log().all()
		.when()
			.get("/todo")
		.then()
			.log().all()
			.statusCode(200)			
		;
	}
	
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.log().all()
			.body("{\r\n"
					+ "	\"task\": \"Teste via api\",\r\n"
					+ "	\"dueDate\": \"2030-12-30\"\r\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(201)			
		;
	}
	
	
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.log().all()
			.body("{\r\n"
					+ "	\"task\": \"Teste via api\",\r\n"
					+ "	\"dueDate\": \"2010-12-30\"\r\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(400)	
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}



/*
{
	"task": "Teste viaa api",
	"dueDate": "2020-12-30"
}
*/


	@Test
	public void deveRemoverTarefaComSucesso() {
		//inserir
		Integer id = RestAssured.given()
			.log().all()
			.body("{\r\n"
					+ "	\"task\": \"Tarefa Teste para remoção - Teste via api\",\r\n"
					+ "	\"dueDate\": \"2030-12-30\"\r\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(201)	
			.extract().path("id") //extra o id retornado na resposta para ser usado pela exclusão
		;
		
		System.out.println(id);
		
		//remover
		RestAssured.given()
			.log().all()
		.when()
			.delete("/todo/"+ id)
		.then()
			.log().all()
			.statusCode(204)
		;
	}
}
