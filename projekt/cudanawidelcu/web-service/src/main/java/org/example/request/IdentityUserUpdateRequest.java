package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.response.IdentityRoleResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentityUserUpdateRequest {
    private Long id;
    private String username;
    private IdentityUserRoleRequest roleRequest;
}
