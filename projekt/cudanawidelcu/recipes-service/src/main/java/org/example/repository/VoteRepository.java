package org.example.repository;

import org.example.model.Vote;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends R2dbcRepository<Vote, Long> {
}
