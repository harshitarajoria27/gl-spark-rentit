export type ResourceCondition =
  | "NEW"
  | "LIKE_NEW"
  | "GOOD"
  | "FAIR"
  | "POOR";


export interface ResourceRequest {
  title: string;
  description: string;
  category: string;
  rentPerDay: number;
  securityDeposit: number;
  quantity: number;
  condition: ResourceCondition;
  city: string;
  state: string;
  imageUrl: string;
}


export interface ResourceUpdateRequest {
  title: string;
  description: string;
  category: string;
  rentPerDay: number;
  securityDeposit: number;
  quantity: number;
  condition: ResourceCondition;
  city: string;
  state: string;
  imageUrl: string;
  available: boolean;
}


export interface ResourceResponse {
  resourceId: number;
  ownerId: number;

  title: string;
  description: string;
  category: string;

  rentPerDay: number;
  securityDeposit: number;
  quantity: number;

  condition: ResourceCondition;
  available: boolean;

  city: string;
  state: string;

  imageUrl: string | null;

  createdDate: string;
  
}