package br.com.project.screenmatch;

import br.com.project.screenmatch.model.SeriesData;
import br.com.project.screenmatch.service.ApiConsumption;
import br.com.project.screenmatch.service.DataConversion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		var apiConsumption = new ApiConsumption();
		var json = apiConsumption.dataObtain("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		DataConversion convert = new DataConversion();
		SeriesData datas = convert.dataObtain(json, SeriesData.class);
		System.out.println(datas);
	}
}