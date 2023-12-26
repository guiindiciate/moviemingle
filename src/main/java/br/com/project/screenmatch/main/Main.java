package br.com.project.screenmatch.main;

import br.com.project.screenmatch.model.Episode;
import br.com.project.screenmatch.model.EpisodesData;
import br.com.project.screenmatch.model.SeasonsData;
import br.com.project.screenmatch.model.SeriesData;
import br.com.project.screenmatch.service.ApiConsumption;
import br.com.project.screenmatch.service.DataConversion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner reading = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConversion convert = new DataConversion();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void menuDisplay() {
        System.out.println("Enter the series name: ");
        var seriesName = reading.nextLine();
        var json = consumption.dataObtain(ADDRESS + seriesName.replace(" ", "+") + API_KEY);

        SeriesData datas = convert.dataObtain(json, SeriesData.class);
        System.out.println(datas);


        List<SeasonsData> seasons = new ArrayList<>();

        for (int i = 1; i <= datas.SeasonsTotal(); i++) {
            json = consumption.dataObtain(ADDRESS + seriesName.replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonsData seasonsData = convert.dataObtain(json, SeasonsData.class);
            seasons.add(seasonsData);
        }
        seasons.forEach(System.out::println);

//        for (int i = 0; i < datas.SeasonsTotal(); i++) {
//            List<EpisodesData> seasonEpisodes = seasons.get(i).episodes();
//            for ( int j = 0; j < seasonEpisodes.size(); j++) {
//                System.out.println(seasonEpisodes.get(j).title());
//            }
//        }
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodesData> episodesData = seasons.stream().flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());
        //.toList();


        System.out.println("\n********Top 10 episodes********");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodesData::rating).reversed())
                .limit(10)
                .map(e -> e.title().toUpperCase())
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.number(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Type an excerpt from the title episode: ");

        var titleExcerpt = reading.nextLine();
        Optional<Episode> searchedEpisode = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(titleExcerpt.toUpperCase()))
                .findFirst();
        if (searchedEpisode.isPresent()) {
            System.out.println("Episode has been found!");
            System.out.println("Season: " + searchedEpisode.get().getSeason());
        } else {
            System.out.println("Episode has not been found!");
        }

//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var year = reading.nextInt();
//        reading.nextLine();
//
//        LocalDate searchData =LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//        episodes.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchData))
//                .forEach(e-> System.out.println(
//                        "Season: " + e.getSeason() +
//                                " Episode: " + e.getTitle() +
//                                "Release date: " + e.getReleaseDate().format(formatter)
//                ));

        Map<Integer, Double> ratingBySeasons = episodes.stream()
                .filter(e-> e.getRating()> 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingBySeasons);

        DoubleSummaryStatistics statistics = episodes.stream()
                .filter(e-> e.getRating()> 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Medium: " + statistics.getAverage());
        System.out.println("Best episode: " + statistics.getMax());
        System.out.println("Worst episode: " + statistics.getMin());
        System.out.println("Quantity: " + statistics.getCount());

    }
}
