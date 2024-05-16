package com.devmitz.screenmatch.principal;

import com.devmitz.screenmatch.model.DadosSerie;
import com.devmitz.screenmatch.model.DadosTemporada;
import com.devmitz.screenmatch.service.ConsumoApi;
import com.devmitz.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=66f9433c&";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void ExibeMenu() {
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = sc.nextLine();

        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> dadosTemporadaList = new ArrayList<>();
		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++ ){
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season="+ i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			dadosTemporadaList.add(dadosTemporada);
		}
		dadosTemporadaList.forEach(System.out::println);

//        for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada =  dadosTemporadaList.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        dadosTemporadaList.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
