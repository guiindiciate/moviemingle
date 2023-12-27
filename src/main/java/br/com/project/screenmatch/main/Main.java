package br.com.project.screenmatch.main;

import br.com.project.screenmatch.model.*;
import br.com.project.screenmatch.repository.RepositorySeries;
import br.com.project.screenmatch.service.ApiConsumption;
import br.com.project.screenmatch.service.DataConversion;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConversion conversion = new DataConversion();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<SeriesData> seriesData = new ArrayList<>();

    private RepositorySeries repository;

    public Main(RepositorySeries repository) {
        this.repository = repository;
    }


    public void menuDisplay() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Search series
                    2 - Search episodes
                    3 - List series
                                    
                    0 - Exit                                 
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    webSearchSeries();
                    break;
                case 2:
                    seriesByEpisodeSearch();
                    break;
                case 3:
                    searchList();
                    break;
                case 0:
                    System.out.println("Leaving...");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void webSearchSeries() {
        SeriesData datas = getSeriesData();
        TvShow tvShow = new TvShow(datas);
        //seriesData.add(datas);
        repository.save(tvShow);
        System.out.println(datas);
    }

    private SeriesData getSeriesData() {
        System.out.println("Enter the name of the series to search");
        var seriesName = scanner.nextLine();
        var json = consumption.dataObtain(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        SeriesData datas = conversion.dataObtain(json, SeriesData.class);
        return datas;
    }

    private void seriesByEpisodeSearch() {
        SeriesData seriesData = getSeriesData();
        List<SeasonsData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.seasonsTotal(); i++) {
            var json = consumption.dataObtain(ADDRESS + seriesData.title().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonsData seasonsData = conversion.dataObtain(json, SeasonsData.class);
            seasons.add(seasonsData);
        }
        seasons.forEach(System.out::println);
    }
    private void searchList() {

        List<TvShow> tvShows = repository.findAll();

//        tvShows.stream()
//                .sorted(Comparator.comparing(TvShow::getGenre, Comparator.nullsLast(Comparator.naturalOrder())))
//                .forEach(System.out::println);

        tvShows.stream()
                .filter(t -> t.getGenre() != null)
                .sorted(Comparator.comparing(TvShow::getGenre))
                .forEach(System.out::println);

    }
}
