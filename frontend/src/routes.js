import { Navigate } from 'react-router-dom';
import DashboardLayout from 'src/components/DashboardLayout';
import MainLayout from 'src/components/MainLayout';
import Account from 'src/pages/Account';
import CustomerList from 'src/pages/CustomerList';
import OrderList from 'src/pages/OrderList.tsx';
import Dashboard from 'src/pages/Dashboard';
import Login from 'src/pages/Login.tsx';
import NotFound from 'src/pages/NotFound';
import ProductList from 'src/pages/ProductList.tsx';
import Register from 'src/pages/Register.tsx';
import Settings from 'src/pages/Settings';
import AddProduct from './pages/AddProduct.tsx';
import PharmacyProductDetails from './pages/PharmarcyProductDetails.tsx';

const routes = [
  {
    path: 'app',
    element: <DashboardLayout />,
    children: [
      { path: 'account', element: <Account /> },
      { path: 'customers', element: <CustomerList /> },
      { path: 'orders', element: <OrderList /> },
      { path: 'dashboard', element: <Dashboard /> },
      { path: 'products', element: <ProductList /> },
      { path: 'settings', element: <Settings /> },
      { path: 'addProduct', element: <AddProduct />},
      { path: 'product/:id', element: <PharmacyProductDetails />},
      { path: '*', element: <Navigate to="/404" /> }
    ]
  },
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: 'login', element: <Login /> },
      { path: 'register', element: <Register /> },
      { path: '404', element: <NotFound /> },
      { path: '/', element: <Navigate to="/app/dashboard" /> },
      { path: '*', element: <Navigate to="/404" /> }
    ]
  }
];

export default routes;
