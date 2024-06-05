package org.example.controller;

import org.example.dto.VoteDto;
import org.example.model.Vote;
import org.example.response.RatingByRecipeIdResponse;
import org.example.service.VoteService;
import org.example.util.VoteMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@EnableWebFlux
@RequestMapping("/api/v1/votes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public Flux<VoteDto> findAll() {
        Flux<Vote> votes = voteService.findAll();
        return votes.map(VoteMapper::convertVoteToVoteDto);
    }

    @GetMapping("/recipe/{recipeId}")
    public Flux<VoteDto> findAllByRecipeId(@PathVariable("recipeId") Long recipeId) {
        Flux<Vote> votes = voteService.findAllByRecipe(recipeId);
        return votes.map(VoteMapper::convertVoteToVoteDto);
    }

    @PostMapping
    public Mono<VoteDto> save(@RequestBody VoteDto voteDto) {
        Vote vote = VoteMapper.convertVoteDtoToVote(voteDto);
        Mono<Vote> createVote = voteService.save(vote);
        return createVote.map(VoteMapper::convertVoteToVoteDto);
    }

    @DeleteMapping("/recipe/{recipeId}")
    public Mono<Void> deleteAllByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return voteService.deleteAllByRecipeId(recipeId);
    }

    @GetMapping("/rating/recipe/{recipeId}")
    public Mono<RatingByRecipeIdResponse> ratingByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return voteService.ratingByRecipeId(recipeId);
    }
}
