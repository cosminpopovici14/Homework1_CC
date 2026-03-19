import math
from models import OptimizationRequest, RouteNodeDto, OptimizationResponse
from typing import List

# Calculam distanta intre 2 puncte folosind formula Haversine (pentru distante pe sferă - pamant)
# Este ceva mai "realista" la prezentarea de licenta fata de distanta Euclidiana
def haversine_distance(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
    R = 6371.0 # Raza aproximativa a Pamantului in km
    
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    
    a = (math.sin(dlat / 2) * math.sin(dlat / 2) +
         math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) *
         math.sin(dlon / 2) * math.sin(dlon / 2))
    
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c
    
    return distance

# Algoritm "Nearest Neighbor" (Cel mai apropiat vecin)
def solve_tsp_nearest_neighbor(data: OptimizationRequest) -> OptimizationResponse:
    depot = data.depot
    unvisited = data.customers.copy()
    
    # Construim punctul de start (Depozitul)
    start_node = RouteNodeDto(id="DEPOT", name=depot.name, lat=depot.lat, lng=depot.lng)
    
    route: List[RouteNodeDto] = [start_node]
    total_distance = 0.0
    
    current_lat = depot.lat
    current_lng = depot.lng
    
    # Cat timp mai avem clienti de vizitat
    while unvisited:
        nearest_customer = None
        min_distance = float('inf')
        
        # Cautam cel mai apropiat client fata de locatia noastra curenta
        for customer in unvisited:
            dist = haversine_distance(current_lat, current_lng, customer.lat, customer.lng)
            if dist < min_distance:
                min_distance = dist
                nearest_customer = customer
                
        # Ne mutam la el
        route.append(RouteNodeDto(
            id=nearest_customer.id,
            name=nearest_customer.name,
            lat=nearest_customer.lat,
            lng=nearest_customer.lng
        ))
        
        total_distance += min_distance
        current_lat = nearest_customer.lat
        current_lng = nearest_customer.lng
        
        unvisited.remove(nearest_customer)
        
    # Ne intoarcem la depozit din ultima locatie
    return_distance = haversine_distance(current_lat, current_lng, depot.lat, depot.lng)
    total_distance += return_distance
    
    # Adaugam depozitul la finalul listei
    route.append(start_node)
    
    return OptimizationResponse(
        totalDistance=total_distance,
        route=route
    )
