package br.com.project.screenmatch.repository;

import br.com.project.screenmatch.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorySeries extends JpaRepository<TvShow, Long> {
}
