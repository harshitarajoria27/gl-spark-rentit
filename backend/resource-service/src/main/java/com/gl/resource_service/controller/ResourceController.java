package com.gl.resource_service.controller;

import com.gl.resource_service.dto.*;
import com.gl.resource_service.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @PostMapping
    public ResponseEntity<ResourceResponse> addResource(
            @RequestHeader("X-User-Id") Long ownerId,
            @RequestBody ResourceRequest request) {

        return ResponseEntity.ok(
                service.addResource(request, ownerId)
        );
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAll(
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity.ok(
                service.getAllResources(userId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getResourceById(id)
        );
    }

    @GetMapping("/my-resources")
    public ResponseEntity<List<ResourceResponse>> getMyResources(
            @RequestHeader("X-User-Id") Long ownerId) {

        return ResponseEntity.ok(
                service.getResourcesByOwner(ownerId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable Long id,
            @RequestBody ResourceUpdateRequest request) {

        return ResponseEntity.ok(
                service.updateResource(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.deleteResource(id);

        return ResponseEntity.ok(
                "Resource deleted successfully"
        );
    }
}