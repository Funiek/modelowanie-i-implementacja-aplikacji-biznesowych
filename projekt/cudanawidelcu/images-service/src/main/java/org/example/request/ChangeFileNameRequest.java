package org.example.request;

import lombok.Data;

@Data
public class ChangeFileNameRequest {
    String oldName;
    String newName;
}
