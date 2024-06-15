package org.example.util;

import org.example.dto.VoteDto;
import org.example.model.Vote;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VoteMapperTest {

    @Test
    void convertVoteToVoteDto_ShouldReturnVoteDto() {
        // Given
        Vote vote = Vote.builder()
                .id(1L)
                .recipeId(2L)
                .rating(4.5)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        VoteDto voteDto = VoteMapper.convertVoteToVoteDto(vote);

        // Then
        assertThat(voteDto).isNotNull();
        assertThat(voteDto.getId()).isEqualTo(vote.getId());
        assertThat(voteDto.getRecipeId()).isEqualTo(vote.getRecipeId());
        assertThat(voteDto.getRating()).isEqualTo(vote.getRating());
    }

    @Test
    void convertVoteDtoToVote_ShouldReturnVote() {
        // Given
        VoteDto voteDto = VoteDto.builder()
                .id(1L)
                .recipeId(2L)
                .rating(4.5)
                .build();

        // When
        Vote vote = VoteMapper.convertVoteDtoToVote(voteDto);

        // Then
        assertThat(vote).isNotNull();
        assertThat(vote.getId()).isEqualTo(voteDto.getId());
        assertThat(vote.getRecipeId()).isEqualTo(voteDto.getRecipeId());
        assertThat(vote.getRating()).isEqualTo(voteDto.getRating());
        assertThat(vote.getCreatedAt()).isNotNull();
    }
}