package org.example.service;

import org.example.request.VotesSaveRequest;
import org.example.response.VotesRatingByRecipeIdResponse;
import org.example.response.VotesSaveResponse;

public interface VotesService {

    VotesRatingByRecipeIdResponse ratingByRecipeId(Long recipeId);

    VotesSaveResponse save(VotesSaveRequest votesSaveRequest);

}