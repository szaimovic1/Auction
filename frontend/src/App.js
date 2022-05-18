import { BrowserRouter as Router } from 'react-router-dom';
import Navbar from '@layouts/navbar/navbar';
import Links from './routes/links';
import Footer from '@layouts/footer/footer';
import './App.scss';
import { useState, createContext } from 'react';
import { isAuthenticated } from './utils/auth';

export const Context = createContext();

function App() {
  const [authenticated, setAuthenticated] = useState(isAuthenticated());

    return (
      <Context.Provider value={ setAuthenticated }>
        <Router >
          <Navbar />
          <div className="main-content">
            <Links />
          <Footer />
          </div>
        </Router>
      </Context.Provider>
    );
}
export default App;
