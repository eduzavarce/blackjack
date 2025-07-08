package dev.eduzavarce.blackjack_api.contexts.ranking.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingDto;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingRepository;

@Service
public class GetScoreboardService {
  private final RankingRepository rankingRepository;
  private static final int DEFAULT_LIMIT = 10;

  @Autowired
  public GetScoreboardService(RankingRepository rankingRepository) {
    this.rankingRepository = rankingRepository;
  }

  public Flux<RankingDto> execute() {
    return execute(DEFAULT_LIMIT);
  }

  public Flux<RankingDto> execute(int limit) {
    return rankingRepository.findTopRankings(limit);
  }
}
