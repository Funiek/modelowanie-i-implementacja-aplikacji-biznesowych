package org.example.response;

import lombok.Data;

@Data
public class UpdateRatingResponse {
    private Double rating;
    private int countVotes;
}
