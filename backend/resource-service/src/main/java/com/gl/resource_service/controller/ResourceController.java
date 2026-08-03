package com.gl.resource_service.controller;
import com.gl.resource_service.dto.*;

import com.gl.resource_service.service.ResourceService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
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
            @RequestBody ResourceRequest request){


        return new ResponseEntity<>(
                service.addResource(request),
                HttpStatus.CREATED
        );

    }





    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAll(){


        return ResponseEntity.ok(
                service.getAllResources()
        );

    }





    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getById(
            @PathVariable Long id){


        return ResponseEntity.ok(
                service.getResourceById(id)
        );

    }





    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ResourceResponse>> getByOwner(
            @PathVariable Long ownerId){


        return ResponseEntity.ok(
                service.getResourcesByOwner(ownerId)
        );

    }





    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable Long id,
            @RequestBody ResourceUpdateRequest request){


        return ResponseEntity.ok(
                service.updateResource(id,request)
        );

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id){


        service.deleteResource(id);


        return ResponseEntity.ok(
                "Resource deleted successfully"
        );

    }

}
