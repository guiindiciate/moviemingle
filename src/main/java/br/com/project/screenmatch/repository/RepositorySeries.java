package br.com.project.screenmatch.repository;

import br.com.project.screenmatch.model.Category;
import br.com.project.screenmatch.model.Episode;
import br.com.project.screenmatch.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RepositorySeries extends JpaRepository<TvShow, Long> {
    Optional<TvShow> findByTitleContainingIgnoreCase(String tvShowName);

    List<TvShow> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String nameActor, Double rating);

    List<TvShow> findTop5ByOrderByRatingDesc();

    List<TvShow> findByGenre(Category category);

//    List<TvShow> findBySeasonsTotalLessThanEqualAndRatingGreaterThanEqual(int seasonsTotal, double rating);
    @Query("SELECT t FROM TvShow t WHERE t.seasonsTotal <= :seasonsTotal AND t.rating >= :rating")
    List<TvShow> tvShowsBySeasonAndRating(int seasonsTotal, double rating);

    @Query("SELECT e FROM TvShow t JOIN t.episodes e WHERE e.title ILIKE %:sectionEpisode%")
    List<Episode> episodesBySection(String sectionEpisode);

    @Query("SELECT e FROM TvShow t JOIN t.episodes e WHERE t = :tvShow ORDER BY e.rating DESC LIMIT 5")
    List<Episode> searchTopEpisodesByTvShow(TvShow tvShow);

    @Query("SELECT e FROM TvShow t JOIN t.episodes e WHERE t = :tvShow AND YEAR(e.releaseDate) >= :releaseYear")
    List<Episode> episodeByTvShowAndYear(TvShow tvShow, int releaseYear);
}
