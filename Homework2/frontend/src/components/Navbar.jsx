import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav style={{ padding: '1rem', background: '#333', color: 'white', display: 'flex', gap: '1rem' }}>
      <Link to="/customers" style={{ color: 'white', textDecoration: 'none' }}>Customers</Link>
      <Link to="/vehicles" style={{ color: 'white', textDecoration: 'none' }}>Vehicles</Link>
      <Link to="/optimization" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>Optimize Route</Link>
    </nav>
  );
}

export default Navbar;
