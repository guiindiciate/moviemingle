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

    private List<TvShow> tvShows = new ArrayList<>();

    private Optional<TvShow> searchTvShow;

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
                    4 - Search series by title
                    5 - Search series by actor
                    6 - Top 5 Tv Shows
                    7 - Top 5 Episodes
                    8 - Search Tv Shows by category
                    9 - Filter Tv Shows
                    10 - Search episodes by section
                    11 - Search episodes from a date
                                    
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
                case 4:
                    searchSeriesByTitle();
                    break;
                case 5:
                    searchSeriesByActor();
                    break;
                case 6:
                    searchTop5TvShows();
                    break;
                case 7:
                    searchTopEpisodesByTvShow();
                    break;
                case 8:
                    searchTvShowsByCategory();
                    break;
                case 9:
                    filterTvShowsBySeasonsAndRating();
                    break;
                case 10:
                    searchEpisodeBySection();
                    break;
                case 11:
                    searchEpisodesFromDate();
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

        searchList();
        System.out.println("Choose a tv show by their name: ");
        var tvShowName = scanner.nextLine();

        Optional<TvShow> tvShow = repository.findByTitleContainingIgnoreCase(tvShowName);
        if (tvShow.isPresent()) {

            var tvShowFound = tvShow.get();
            List<SeasonsData> seasons = new ArrayList<>();

            for (int i = 1; i <= tvShowFound.getSeasonsTotal(); i++) {
                var json = consumption.dataObtain(ADDRESS + tvShowFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                SeasonsData seasonsData = conversion.dataObtain(json, SeasonsData.class);
                seasons.add(seasonsData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());
            tvShowFound.setEpisodes(episodes);
            repository.save(tvShowFound);

        } else {
            System.out.println("Tv show has not been found!");
        }
    }

    private void searchList() {

        tvShows = repository.findAll();

//        tvShows.stream()
//                .sorted(Comparator.comparing(TvShow::getGenre, Comparator.nullsLast(Comparator.naturalOrder())))
//                .forEach(System.out::println);
        tvShows.stream()
                .filter(t -> t.getGenre() != null)
                .sorted(Comparator.comparing(TvShow::getGenre))
                .forEach(System.out::println);

    }

    private void searchSeriesByTitle() {

        System.out.println("Choose a tv show by their name: ");
        var tvShowName = scanner.nextLine();
        searchTvShow = repository.findByTitleContainingIgnoreCase(tvShowName);

        if (searchTvShow.isPresent()) {
            System.out.println("Tv Show data: " + searchTvShow.get());
        } else {
            System.out.println("Tv Show not found!");
        }
    }

    private void searchSeriesByActor() {

        System.out.println("What name do you want to search? ");
        var nameActor = scanner.nextLine();
        System.out.println("Rating starting at what value: ");
        var rating = scanner.nextDouble();
        List<TvShow> foundSeries = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(nameActor, rating);
        System.out.println("Tv shows " + nameActor + " has worked:");
        foundSeries.forEach(f ->
                System.out.println(f.getTitle() + " rating: " + f.getRating()));
    }

    private void searchTop5TvShows() {

        List<TvShow> topTvShow = repository.findTop5ByOrderByRatingDesc();
        topTvShow.forEach(t ->
                System.out.println(t.getTitle() + " rating: " + t.getRating()));
    }

    private void searchTopEpisodesByTvShow() {
        searchSeriesByTitle();

        if (searchTvShow.isPresent()) {
            TvShow tvShow = searchTvShow.get();
            List<Episode> topEpisodes = repository.searchTopEpisodesByTvShow(tvShow);
            topEpisodes.forEach(f ->
                    System.out.printf("Tv Show: %s Season %s - Episode %s - %s Rating %s\n",
                            f.getTvShow().getTitle(), f.getSeason(),
                            f.getNumberEpisode(), f.getTitle(), f.getRating()));
        }
    }

    private void searchTvShowsByCategory() {

        System.out.println("Would like to search tv shows from what category/genre? ");
        var genreName = scanner.nextLine();
        Category category = Category.fromString(genreName);
//        Category category = Category.fromPortuguese(genreName); //IN CASE A PT/BR PERSON WANTS TO USE THIS SOFTWARE
        List<TvShow> tvShowsByCategory = repository.findByGenre(category);

        System.out.println("Tv show from category" + genreName);
        tvShowsByCategory.forEach(System.out::println);
    }

    private void filterTvShowsBySeasonsAndRating() {

        System.out.println("How many seasons would you like to filter?");
        var seasonsTotal = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Starting at what rating? ");
        var rating = scanner.nextDouble();
        scanner.nextLine();
//        List<TvShow> tvShowsFilter = repository.findBySeasonsTotalLessThanEqualAndRatingGreaterThanEqual(seasonsTotal, rating);
        List<TvShow> tvShowsFilter = repository.tvShowsBySeasonAndRating(seasonsTotal, rating);
        System.out.println("**** Filtered TV SHOWS ****");
        tvShowsFilter.forEach(t ->
                System.out.println(t.getTitle() + " - rating: " + t.getRating()));
    }

    private void searchEpisodeBySection() {

        System.out.println("What episode's name would you like to search? ");
        var sectionEpisode = scanner.nextLine();

        List<Episode> foundEpisodes = repository.episodesBySection(sectionEpisode);
        foundEpisodes.forEach(f ->
                System.out.printf("Tv Show: %s Season %s - Episode %s - %s\n",
                        f.getTvShow().getTitle(), f.getSeason(),
                        f.getNumberEpisode(), f.getTitle()));
    }

    private void searchEpisodesFromDate() {
        searchSeriesByTitle();

        if (searchTvShow.isPresent()) {
            TvShow tvShow = searchTvShow.get();
            System.out.println("Enter the year of release: ");
            var releaseYear = scanner.nextInt();
            scanner.nextLine();

            List<Episode> yearEpisodes = repository.episodeByTvShowAndYear(tvShow, releaseYear);
            yearEpisodes.forEach(System.out::println);
        }
    }
}
