import { useState, useEffect } from 'react';
import { vehicleService } from '../services/vehicleService';

function VehiclesPage() {
  const [vehicles, setVehicles] = useState([]);
  const [formData, setFormData] = useState({ registrationNumber: '', capacity: '' });

  useEffect(() => {
    loadVehicles();
  }, []);

  const loadVehicles = () => {
    vehicleService.getAll().then(setVehicles).catch(console.error);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      registrationNumber: formData.registrationNumber,
      capacity: parseInt(formData.capacity)
    };

    vehicleService.create(payload)
      .then(() => {
        loadVehicles();
        setFormData({ registrationNumber: '', capacity: '' });
      })
      .catch(console.error);
  };

  const handleDelete = (id) => {
    vehicleService.delete(id).then(loadVehicles).catch(console.error);
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Vehicles Management</h2>
      
      <form onSubmit={handleSubmit} style={{ marginBottom: '2rem', display: 'flex', gap: '1rem' }}>
        <input placeholder="Registration Number" value={formData.registrationNumber} onChange={e => setFormData({...formData, registrationNumber: e.target.value})} required />
        <input type="number" placeholder="Capacity" value={formData.capacity} onChange={e => setFormData({...formData, capacity: e.target.value})} required />
        <button type="submit">Add Vehicle</button>
      </form>

      <table border="1" cellPadding="10" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Registration</th>
            <th>Capacity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {vehicles.map(v => (
            <tr key={v.id}>
              <td>{v.id}</td>
              <td>{v.registrationNumber}</td>
              <td>{v.capacity}</td>
              <td>
                <button onClick={() => handleDelete(v.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default VehiclesPage;
