import { useState } from 'react';
import { optimizationService } from '../services/optimizationService';

function OptimizationPage() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSolve = () => {
    setLoading(true);
    setError(null);
    setResult(null);

    optimizationService.solve()
      .then(data => {
        setResult(data);
      })
      .catch(err => {
        console.error(err);
        setError("Error solving route. Ensure backend and Python service are running, and you have at least 1 customer and 1 vehicle.");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Route Optimization</h2>
      <button 
        onClick={handleSolve} 
        disabled={loading}
        style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}
      >
        {loading ? 'Calculating...' : 'Solve Route'}
      </button>

      {error && <div style={{ color: 'red', marginTop: '1rem' }}>{error}</div>}

      {result && (
        <div style={{ marginTop: '2rem', padding: '1rem', border: '1px solid #ccc' }}>
          <h3>Optimization Result</h3>
          <p><strong>Total Distance:</strong> {result.totalDistance.toFixed(2)} km</p>
          
          <h4>Route Order:</h4>
          <ol>
            {result.route.map((node, index) => (
              <li key={index}>
                <strong>{node.name}</strong> 
                {node.id !== 'DEPOT' && ` (Customer ID: ${node.id})`}
                <br/>
                <small>Lat: {node.lat}, Lng: {node.lng}</small>
              </li>
            ))}
          </ol>
        </div>
      )}
    </div>
  );
}

export default OptimizationPage;
