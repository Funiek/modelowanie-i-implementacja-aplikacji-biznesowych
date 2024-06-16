package org.example.util;

import org.example.dto.VoteDto;
import org.example.model.Vote;

import java.time.LocalDateTime;

/**
 * Mapper class for converting between Vote and VoteDto.
 */
public class VoteMapper {

    /**
     * Converts Vote entity to VoteDto.
     *
     * @param vote the Vote entity to convert
     * @return the converted VoteDto
     */
    public static VoteDto convertVoteToVoteDto(Vote vote) {
        return VoteDto.builder()
                .id(vote.getId())
                .recipeId(vote.getRecipeId())
                .rating(vote.getRating())
                .build();
    }

    /**
     * Converts VoteDto to Vote entity.
     *
     * @param voteDto the VoteDto to convert
     * @return the converted Vote entity
     */
    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        return Vote.builder()
                .id(voteDto.getId())
                .recipeId(voteDto.getRecipeId())
                .rating(voteDto.getRating())
                .createdAt(LocalDateTime.now())
                .build();
    }
}