import api from "./axios";

import type {
  ResourceRequest,
  ResourceResponse,
  ResourceUpdateRequest
} from "../types/resource";


/* =========================================
   ADD RESOURCE
========================================= */

export const addResource = async (
  data: ResourceRequest
): Promise<ResourceResponse> => {

  const response =
    await api.post<ResourceResponse>(
      "/resources",
      data
    );

  return response.data;
};


/* =========================================
   GET ALL RESOURCES
========================================= */

export const getAllResources =
  async (): Promise<ResourceResponse[]> => {

    const response =
      await api.get<ResourceResponse[]>(
        "/resources"
      );

    return response.data;
  };


/* =========================================
   GET RESOURCE BY ID
========================================= */

export const getResourceById = async (
  id: number
): Promise<ResourceResponse> => {

  const response =
    await api.get<ResourceResponse>(
      `/resources/${id}`
    );

  return response.data;
};


/* =========================================
   GET MY RESOURCES
========================================= */

export const getMyResources =
  async (): Promise<ResourceResponse[]> => {

    const response =
      await api.get<ResourceResponse[]>(
        "/resources/my-resources"
      );

    return response.data;
  };


/* =========================================
   UPDATE RESOURCE
========================================= */

export const updateResource = async (
  id: number,
  data: ResourceUpdateRequest
): Promise<ResourceResponse> => {

  const response =
    await api.put<ResourceResponse>(
      `/resources/${id}`,
      data
    );

  return response.data;
};


/* =========================================
   DELETE RESOURCE
========================================= */

export const deleteResource = async (
  id: number
): Promise<void> => {

  await api.delete(
    `/resources/${id}`
  );
};