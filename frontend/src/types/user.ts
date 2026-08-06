export interface UserResponse {
  id: number;
  fullName: string;
  email: string;

  phone: string | null;
  profileImage: string | null;
  bio: string | null;
  address: string | null;
  city: string | null;
  state: string | null;
  pincode: string | null;
}

export interface UpdateProfileRequest {
  phone: string;
  profileImage: string;
  bio: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
}