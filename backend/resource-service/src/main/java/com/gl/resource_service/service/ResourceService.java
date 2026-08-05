package com.gl.resource_service.service;




import com.gl.resource_service.dto.*;

import java.util.List;


public interface ResourceService {


    ResourceResponse addResource(
            ResourceRequest request,Long ownerId);



    ResourceResponse getResourceById(
            Long id);



    List<ResourceResponse> getAllResources();



    List<ResourceResponse> getResourcesByOwner(
            Long ownerId);



    ResourceResponse updateResource(
            Long id,
            ResourceUpdateRequest request);



    void deleteResource(Long id);

}
