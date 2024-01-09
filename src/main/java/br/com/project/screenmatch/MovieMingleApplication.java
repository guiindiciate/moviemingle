package br.com.project.screenmatch;

import br.com.project.screenmatch.main.Main;
import br.com.project.screenmatch.repository.RepositorySeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieMingleApplication implements CommandLineRunner {

	@Autowired
	private RepositorySeries repository;

	public static void main(String[] args) {
		SpringApplication.run(MovieMingleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Main main = new Main(repository);
		main.menuDisplay();
	}
}