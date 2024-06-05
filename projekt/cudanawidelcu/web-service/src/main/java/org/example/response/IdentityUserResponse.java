package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentityUserResponse {
    private Long id;
    private String username;
    private IdentityRoleResponse identityRoleResponse;
}
