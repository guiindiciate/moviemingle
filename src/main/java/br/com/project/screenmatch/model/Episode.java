package br.com.project.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    Integer numberEpisode;
    private Double rating;
    private LocalDate releaseDate;
    @ManyToOne
    private TvShow tvShow;

    public Episode(Integer numberSeason, EpisodesData episodesData) {
        this.season = numberSeason;
        this.title = episodesData.title();
        this.numberEpisode = episodesData.number();
        try {
            this.rating = Double.valueOf(episodesData.rating());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(episodesData.releaseDate());
        } catch (DateTimeParseException ex) {
            this.releaseDate = null;
        }
    }

    public Episode() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TvShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumberEpisode() {
        return numberEpisode;
    }

    public void setNumberEpisode(Integer numberEpisode) {
        this.numberEpisode = numberEpisode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public String toString() {
        return  "season=" + season +
                ", title='" + title + '\'' +
                ", numberEpisode=" + numberEpisode +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate;
    }
}
