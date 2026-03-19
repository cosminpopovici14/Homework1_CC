from pydantic import BaseModel
from typing import List

# --- Modele de Request (in: date trimise de la Spring Boot) ---

class DepotDto(BaseModel):
    name: str
    lat: float
    lng: float

class VehicleDto(BaseModel):
    id: str
    capacity: int

class CustomerDto(BaseModel):
    id: str
    name: str
    lat: float
    lng: float
    demand: int

class OptimizationRequest(BaseModel):
    depot: DepotDto
    vehicle: VehicleDto
    customers: List[CustomerDto]

# --- Modele de Response (out: rezultatul calculat si trimis inapoi) ---

class RouteNodeDto(BaseModel):
    id: str
    name: str
    lat: float
    lng: float

class OptimizationResponse(BaseModel):
    totalDistance: float
    route: List[RouteNodeDto]
