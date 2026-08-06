import api from "./axios";

import type {
  UserResponse,
  UpdateProfileRequest
} from "../types/user";


export const getProfile =
  async (): Promise<UserResponse> => {

    const response =
      await api.get<UserResponse>(
        "/users/profile"
      );

    return response.data;
  };


export const updateProfile = async (
  email: string,
  data: UpdateProfileRequest
): Promise<UserResponse> => {

  const response =
    await api.put<UserResponse>(
      "/users/profile",
      data,
      {
        params: {
          email
        }
      }
    );

  return response.data;
};