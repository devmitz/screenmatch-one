package com.devmitz.screenmatch;

import com.devmitz.screenmatch.model.DadosEpisodio;
import com.devmitz.screenmatch.model.DadosSerie;
import com.devmitz.screenmatch.service.ConsumoApi;
import com.devmitz.screenmatch.service.ConverteDados;
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
		System.out.println("Projeto Java Spring sem Web");
		var consumoApi = new ConsumoApi();


		var json =  consumoApi.obterDados("http://www.omdbapi.com/?t=breaking+bad&apikey=66f9433c&");
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterDados("http://www.omdbapi.com/?t=breaking+bad&season=1&episode=5&apikey=66f9433c&");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);


	}
}
