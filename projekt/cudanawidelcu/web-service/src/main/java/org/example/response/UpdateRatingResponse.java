package pl.cudanawidelcu.microservices.services.web.Response;

import lombok.Data;

@Data
public class UpdateRatingResponse {
    private Double rating;
    private int countVotes;
}
