import { useState, useEffect } from 'react';
import { customerService } from '../services/customerService';

function CustomersPage() {
  const [customers, setCustomers] = useState([]);
  const [formData, setFormData] = useState({ name: '', addressText: '', demand: 0, lat: '', lng: '' });

  useEffect(() => {
    loadCustomers();
  }, []);

  const loadCustomers = () => {
    customerService.getAll().then(setCustomers).catch(console.error);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      name: formData.name,
      addressText: formData.addressText,
      demand: parseInt(formData.demand),
      coordinates: { latitude: parseFloat(formData.lat), longitude: parseFloat(formData.lng) }
    };

    customerService.create(payload)
      .then(() => {
        loadCustomers();
        setFormData({ name: '', addressText: '', demand: 0, lat: '', lng: '' });
      })
      .catch(console.error);
  };

  const handleDelete = (id) => {
    customerService.delete(id).then(loadCustomers).catch(console.error);
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Customers Management</h2>
      
      <form onSubmit={handleSubmit} style={{ marginBottom: '2rem', display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
        <input placeholder="Name" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} required />
        <input placeholder="Address" value={formData.addressText} onChange={e => setFormData({...formData, addressText: e.target.value})} required />
        <input type="number" placeholder="Demand" value={formData.demand} onChange={e => setFormData({...formData, demand: e.target.value})} required />
        <input type="number" step="0.0001" placeholder="Latitude" value={formData.lat} onChange={e => setFormData({...formData, lat: e.target.value})} required />
        <input type="number" step="0.0001" placeholder="Longitude" value={formData.lng} onChange={e => setFormData({...formData, lng: e.target.value})} required />
        <button type="submit">Add Customer</button>
      </form>

      <table border="1" cellPadding="10" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Address</th>
            <th>Demand</th>
            <th>Lat/Lng</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(c => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.name}</td>
              <td>{c.addressText}</td>
              <td>{c.demand}</td>
              <td>{c.coordinates?.latitude}, {c.coordinates?.longitude}</td>
              <td>
                <button onClick={() => handleDelete(c.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default CustomersPage;
