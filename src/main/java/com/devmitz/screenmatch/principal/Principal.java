package com.devmitz.screenmatch.principal;

import com.devmitz.screenmatch.model.DadosEpisodio;
import com.devmitz.screenmatch.model.DadosSerie;
import com.devmitz.screenmatch.model.DadosTemporada;
import com.devmitz.screenmatch.model.Episodio;
import com.devmitz.screenmatch.service.ConsumoApi;
import com.devmitz.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        List<DadosEpisodio> dadosEpisodios = dadosTemporadaList.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios: ");

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("INFO | Primeiro filtro (N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("PEEK | Sorted: " + e))
                .limit(5)
                .forEach(System.out::println);


        System.out.println("\nEpisodios:");
        List<Episodio> episodios = dadosTemporadaList.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);


        System.out.println("A partir de qual ano você deseja ver os episódios?");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataLancamento()
                ));


    }
}
