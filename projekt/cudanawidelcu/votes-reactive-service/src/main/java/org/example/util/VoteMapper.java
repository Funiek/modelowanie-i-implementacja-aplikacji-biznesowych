package org.example.util;

import org.example.dto.VoteDto;
import org.example.model.Vote;

import java.time.LocalDateTime;

public class VoteMapper {
    public static VoteDto convertVoteToVoteDto(Vote vote) {
        return VoteDto.builder()
                .id(vote.getId())
                .recipeId(vote.getRecipeId())
                .rating(vote.getRating())
                .build();
    }

    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        return Vote.builder()
                .id(voteDto.getId())
                .recipeId(voteDto.getRecipeId())
                .rating(voteDto.getRating())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
