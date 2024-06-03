package org.example.service;

import org.example.model.Vote;
import org.example.repository.VoteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Flux<Vote> findAll() {
        return voteRepository.findAll();
    }

    @Override
    public Flux<Vote> findAllByRecipe(Long recipeId) {
        return voteRepository.findAllByRecipeId(recipeId);
    }

    @Override
    public Mono<Vote> save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return voteRepository.deleteAllByRecipeId(recipeId);
    }
}