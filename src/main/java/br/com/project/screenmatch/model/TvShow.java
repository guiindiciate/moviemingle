package br.com.project.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "tvshows")
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer seasonsTotal;
    private Double rating;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private String actors;
    private String poster;
    private String plot;
    @OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();

    public TvShow() {}

    public TvShow(SeriesData seriesData) {
        this.title = seriesData.title();
        this.seasonsTotal = seriesData.seasonsTotal();
        this.rating = OptionalDouble.of(Double.valueOf(seriesData.rating())).orElse(0);
        this.genre = Category.fromString(seriesData.genre().split(",")[0].trim());
        this.actors = seriesData.actors();
        this.poster = seriesData.poster();
        this.plot = seriesData.plot();
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(e -> e.setTvShow(this));
        this.episodes = episodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeasonsTotal() {
        return seasonsTotal;
    }

    public void setSeasonsTotal(Integer seasonsTotal) {
        this.seasonsTotal = seasonsTotal;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return
                "genre=" + genre +
                ", title='" + title + '\'' +
                ", seasonsTotal=" + seasonsTotal +
                ", rating=" + rating +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", plot='" + plot + '\'' +
                ", episodes='" + episodes + '\'';
    }

}