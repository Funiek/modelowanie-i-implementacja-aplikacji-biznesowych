package org.example.Response;

import lombok.Data;

@Data
public class UpdateRatingResponse {
    private Double rating;
    private int countVotes;
}
