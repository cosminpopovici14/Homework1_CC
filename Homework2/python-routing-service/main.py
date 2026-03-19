from fastapi import FastAPI
from models import OptimizationRequest, OptimizationResponse
from solver import solve_tsp_nearest_neighbor

app = FastAPI(title="Vehicle Routing Optimization API")

@app.post("/solve", response_model=OptimizationResponse)
def solve_route(request: OptimizationRequest):
    """
    Primeste starea curenta (Depozit, Masina, Clienti) de la Spring Boot
    si returneaza un traseu optimizat calculat cu metoda Nearest Neighbor.
    """
    result = solve_tsp_nearest_neighbor(request)
    return result

# Pentru a rula serverul:
# uvicorn main:app --reload --port 8000
